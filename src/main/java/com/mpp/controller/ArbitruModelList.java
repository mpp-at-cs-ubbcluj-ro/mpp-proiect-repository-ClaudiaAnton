package com.mpp.controller;

import com.mpp.domain.Arbitru;
import com.mpp.domain.Rezultat;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ArbitruModelList extends AbstractListModel {
    List<Arbitru> listModel;

    public ArbitruModelList() {
        this.listModel = new ArrayList<>();
    }

    public void newArbitru(Arbitru arbitru){
        listModel.add(arbitru);
        fireContentsChanged(this, listModel.size()-1, listModel.size());
    }

    public void reset(){
        listModel.clear();
    }

    @Override
    public int getSize() {
        return listModel.size();
    }

    @Override
    public Object getElementAt(int index) {
        return listModel.get(index);
    }
}
