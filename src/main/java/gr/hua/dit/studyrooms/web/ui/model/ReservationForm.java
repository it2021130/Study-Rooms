package gr.hua.dit.studyrooms.web.ui.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class ReservationForm {

    @NotNull @Positive
    private Long roomId;

    @NotNull
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate date;

    @Positive
    private Integer seatsRequested;

    public ReservationForm() {}


    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getSeatsRequested() { return seatsRequested; }
    public void setSeatsRequested(Integer seatsRequested) { this.seatsRequested = seatsRequested; }

}
