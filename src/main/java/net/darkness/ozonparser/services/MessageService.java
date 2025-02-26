package net.darkness.ozonparser.services;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.darkness.ozonparser.amqp.OcrAmqpConfig;
import net.darkness.ozonparser.models.*;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {
    private final ImageParserService imageParserService;
    private final PriceParser priceParser;
    private final AmqpTemplate amqpTemplate;
    private final OcrAmqpConfig ocrAmqpConfig;

    @RabbitListener(queues = "${rabbitmq.consumer.queue.single}")
    public void parseImageWithAmqp(String message) {
        RequestMessage requestMessage = new Gson().fromJson(message, RequestMessage.class);

        log.info("Preparing message for publishing");
        String result = imageParserService.parseFile(requestMessage.getImage());

        ResponseMessage responseMessage = ResponseMessage.builder()
                .id(requestMessage.getId())
                .parsedImage(result).build();

        String jsonMessage = new Gson().toJson(responseMessage);

        log.info("Publishing message");
        sendMessageWithAmqp(jsonMessage);
        log.info("Message successfully published");
    }

    @RabbitListener(queues = "${rabbitmq.consumer.queue.multiple}")
    public void parseMultipleImagesWithAmqp(String message) {
        RequestMessageMultiple requestMessage = new Gson().fromJson(message, RequestMessageMultiple.class);

        log.info("Preparing message for publishing");
        float sum = 0;
        List<Float> prices = new ArrayList<>();
        for (var image : requestMessage.getFilesBytes()) {
            float price = priceParser.parsePrice(image);
            prices.add(price);
            sum += price;
        }

        ResponseMessageMultiple responseMessage = ResponseMessageMultiple.builder()
                .id(requestMessage.getOrderId())
                .finalPrice(sum).prices(prices).build();

        String jsonMessage = new Gson().toJson(responseMessage);
        log.info("Publishing message");
        sendMessageWithAmqpMultiple(jsonMessage);
        log.info("Message successfully published");
    }


    private void sendMessageWithAmqp(String message) {
        amqpTemplate.convertAndSend(ocrAmqpConfig.getExchange(), ocrAmqpConfig.getRoutingKeyOzon(), message);
    }

    private void sendMessageWithAmqpMultiple(String message) {
        amqpTemplate.convertAndSend(ocrAmqpConfig.getExchange(), ocrAmqpConfig.getRoutingKeyOzonMultiple(), message);
    }
}
