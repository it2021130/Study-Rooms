package gr.hua.dit.studyrooms.core.service.model;

/**
 * Result DTO representing the outcome of a study room update.
 */
public record UpdateStudyRoomResult(
        boolean updated,
        String reason,
        StudyRoomView studyRoomView
) {
    public static UpdateStudyRoomResult success(StudyRoomView view) {
        return new UpdateStudyRoomResult(true, null, view);
    }

    public static UpdateStudyRoomResult fail(String reason) {
        return new UpdateStudyRoomResult(false, reason, null);
    }
}