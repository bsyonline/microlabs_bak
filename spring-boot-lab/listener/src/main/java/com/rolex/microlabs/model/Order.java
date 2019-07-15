package com.rolex.microlabs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author rolex
 * @Since 15/07/2019
 */
@Data
@AllArgsConstructor
@ToString
public class Order {
    int id;
    int userId;
    LocalDateTime idt;
}
