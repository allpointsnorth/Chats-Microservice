/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.repositories;

import com.apnuk.services.chat.entities.TemplateEntity;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author gilesthompson
 */
public interface TemplatesRepository extends CrudRepository<TemplateEntity,Integer> {
    
}
