package com.etoxto.lab1.model.issue;

import com.etoxto.lab1.model.procurement.Procurement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Procurement procurement;

    @Enumerated(EnumType.STRING)
    private ExtendedTime extendedTime;

    @Enumerated(EnumType.STRING)
    private IssueStatus issueStatus;

    @Enumerated(EnumType.STRING)
    private Additionally additionally;

    public Issue(Procurement procurement,
                 ExtendedTime extendedTime,
                 IssueStatus issueStatus,
                 Additionally additionally) {
        this.procurement = procurement;
        this.extendedTime = extendedTime;
        this.issueStatus = issueStatus;
        this.additionally = additionally;
    }
}
