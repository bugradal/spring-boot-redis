package com.kadal.entity;

import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Data
public class Note extends Base implements Serializable {

    private String title;

    private String detail;

}
