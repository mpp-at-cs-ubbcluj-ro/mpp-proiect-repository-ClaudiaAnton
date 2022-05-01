package com.mpp.controller;

import com.mpp.domain.Rezultat;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class RezultatModelList extends AbstractListModel {
    List<Rezultat> list;

    public RezultatModelList() {
        list=new ArrayList<>();
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public Object getElementAt(int index) {
        return list.get(index);
    }

    public void newRezultat(Rezultat rezultat){
        list.add(rezultat);
        fireContentsChanged(this, list.size()-1, list.size());
    }
}
