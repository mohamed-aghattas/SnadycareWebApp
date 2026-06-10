package com.orca.sndycareV99.entity;


import com.orca.sndycareV99.interfaces.BelongsToResidence;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "meeting_participants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingParticipant implements BelongsToResidence {
    @Override
    public Residence getResidence() {
        return null;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "meeting_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_mp_meeting"))
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_mp_member"))
    private Member member;

    @Column(nullable = false)
    @Builder.Default
    private Boolean present = false;
}