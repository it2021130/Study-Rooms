package gr.hua.dit.studyrooms.core.service;

import gr.hua.dit.studyrooms.core.model.PersonType;
import gr.hua.dit.studyrooms.core.model.StudyRoom;
import gr.hua.dit.studyrooms.core.repository.StudyRoomRepository;
import gr.hua.dit.studyrooms.core.service.model.CreatePersonRequest;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Initializes application.
 */
@Service
public class InitializationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitializationService.class);
    private final PersonService personService;
    private final AtomicBoolean initialized;
    private final StudyRoomRepository studyRoomRepository;

    public InitializationService(final PersonService personService, StudyRoomRepository studyRoomRepository) {
        if (personService == null) throw new NullPointerException();
        if (studyRoomRepository == null) throw new NullPointerException();
        this.personService = personService;
        this.initialized = new AtomicBoolean(false);
        this.studyRoomRepository = studyRoomRepository;

    }

    @PostConstruct
    public void populateDatabaseWithInitialData() {
        final boolean alreadyInitialized = this.initialized.getAndSet(true);
        if (alreadyInitialized) {
            LOGGER.warn("Database initialization skipped: initial data has already been populated.");
            return;
        }
        LOGGER.info("Starting database initialization with initial data...");
        final List<CreatePersonRequest> createPersonRequestList = List.of(
                new CreatePersonRequest(
                        PersonType.STAFF,
                        "t0001",
                        "Aggelos",
                        "Gkiose",
                        "aggelos@hua.gr",
                        "+306900000000",
                        "1234"
                ),
                new CreatePersonRequest(
                        PersonType.STUDENT,
                        "it2023001",
                        "Test 1",
                        "Test 1",
                        "it2023001@hua.gr",
                        "+306900000001",
                        "1234"
                ),
                new CreatePersonRequest(
                        PersonType.STUDENT,
                        "it2023002",
                        "Test 2",
                        "Test 2",
                        "it2023002@hua.gr",
                        "+306900000002",
                        "1234"
                )
        );
        for (final var createPersonRequest : createPersonRequestList) {
            this.personService.createPerson(createPersonRequest, false); // do not send SMS
        }

        StudyRoom r1 = new StudyRoom();
        r1.setName("Study Room1");
        r1.setCapacity(10);
        r1.setOpeningTime(LocalTime.parse("08:00"));
        r1.setClosingTime(LocalTime.parse("20:00"));

        StudyRoom r2 = new StudyRoom();
        r2.setName("Study Room2");
        r2.setCapacity(5);
        r2.setOpeningTime(LocalTime.parse("09:00"));
        r2.setClosingTime(LocalTime.parse("22:00"));

        studyRoomRepository.save(r1);
        studyRoomRepository.save(r2);

        LOGGER.info("Database initialization completed.");
    }
}