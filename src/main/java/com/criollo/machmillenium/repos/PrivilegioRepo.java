package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.Privilegio;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PrivilegioRepo {
    private final Session sesion;
    public PrivilegioRepo() {
        sesion = HibernateUtil.getSessionFactory().openSession();
    }
}
