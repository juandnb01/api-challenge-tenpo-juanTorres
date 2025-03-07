package org.jptorres.api_challengeTenpo.utils;

import org.springframework.stereotype.Service;

/*
 * @author Juan Pablo Torres
 * @version 1.0
 */
@Service
public class MockgetAdditionalPercentage {

    public double getAdditionalPercentage(){
        //Se puede reemplazar por una llamada real a un servicio externo si se requiere
        return 0.10;//10% fijo para el mock
    }
}
