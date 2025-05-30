package com.conference.joinusapi.config;

import com.conference.joinusapi.model.Event;
import com.conference.joinusapi.repository.EventRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final EventRepository eventRepository;

    public DataSeeder(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void run(String... args) {
        if (eventRepository.count() > 0) return;

        Faker faker = new Faker(new Locale("en-US"));
        Random random = new Random();

        int numberOfEvents = random.nextInt(501) + 500;

        List<Event> events = new ArrayList<>();

        for (int i = 0; i < numberOfEvents; i++) {
            LocalDateTime dateTime = faker.date()
                    .future(30, java.util.concurrent.TimeUnit.DAYS)
                    .toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDateTime();

            String eventType = randomEventType();

            Event event = Event.builder()
                    .name(faker.book().title())
                    .type(eventType)
                    .location(faker.address().fullAddress())
                    .dateTime(dateTime)
                    .description(faker.lorem().sentence(10))
                    .price(BigDecimal.valueOf(faker.number().randomDouble(2, 0, 100)))
                    .imageUrl(generateImageUrlByType(eventType, faker))
                    .build();

            events.add(event);
        }

        eventRepository.saveAll(events);
        System.out.println("Seeded " + numberOfEvents + " events");
    }

    private String randomEventType() {
        String[] types = {"Tech Conference", "Concert", "Workshop", "Meetup", "Seminar"};
        return types[new Random().nextInt(types.length)];
    }

    private String generateImageUrlByType(String eventType, Faker faker) {
        String keyword;

        switch (eventType.toLowerCase()) {
            case "tech conference" -> keyword = "technology";
            case "concert" -> keyword = "concert";
            case "workshop" -> keyword = "workshop";
            case "meetup" -> keyword = "people";
            case "seminar" -> keyword = "presentation";
            default -> keyword = "event";
        }

        String seed = keyword + "-" + faker.number().digits(5);
        return String.format("https://picsum.photos/seed/%s/800/600", seed);
    }

}
