package gr.hua.dit.studyrooms.core.service.model;

/**
 * CreateStudyRoomResult DTO.
 *
 * @see gr.hua.dit.studyrooms.core.service.impl.StudyRoomServiceImpl#createStudyRoom(CreateStudyRoomRequest) 
 */
public record  CreateStudyRoomResult(
        boolean created,
        String reason,
        StudyRoomView studyRoomView
) {

    public static CreateStudyRoomResult success(final StudyRoomView studyRoomView) {
        if (studyRoomView == null) throw new NullPointerException();
        return new CreateStudyRoomResult(true, null, studyRoomView);
    }

    public static CreateStudyRoomResult fail(final String reason) {
        if (reason == null) throw new NullPointerException();
        if (reason.isBlank()) throw new IllegalArgumentException();
        return new CreateStudyRoomResult(false, reason, null);
    }
}
