package gr.hua.dit.studyrooms.core.repository;

import gr.hua.dit.studyrooms.core.model.StudyRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long> {


    // Check unique name
    boolean existsByNameIgnoreCase(String name);

    Optional<StudyRoom> findById(Long id);
}
