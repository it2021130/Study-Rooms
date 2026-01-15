package gr.hua.dit.studyrooms.core.service;

import gr.hua.dit.studyrooms.core.service.model.StudyRoomView;

import java.util.List;
/**
 * Service for managing {@code StudyRoom} for data analytics purposes.
 */
public interface StudyRoomDataService {
    List<StudyRoomView> getAllRooms();
}
