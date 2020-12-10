package org.example.app.jpa.h2entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "LEGAL_PARTY")
public class LegalParty extends EntityObject {
    @With
    private String name;
    @With
    private String inn;
    @With
    private String kpp;
    @With
    private String ogrn;
    @With
    private String account;
    @With
    private String segment;
    @With
    private String idcrm;

    @Builder
    public LegalParty(String name, String inn, String kpp, String ogrn, String account, String segment, String idcrm) {
        this.name = name;
        this.inn = inn;
        this.kpp = kpp;
        this.ogrn = ogrn;
        this.account = account;
        this.segment = segment;
        this.idcrm = idcrm;
    }
}
