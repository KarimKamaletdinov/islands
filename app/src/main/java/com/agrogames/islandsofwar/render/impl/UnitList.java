package com.agrogames.islandsofwar.render.impl;

import android.os.Build;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.transport.TransportUnit;
import com.agrogames.islandsofwar.engine.abs.weapon.IWeapon;
import com.agrogames.islandsofwar.ui.abs.Element;
import com.agrogames.islandsofwar.ui.abs.ElementType;
import com.agrogames.islandsofwar.ui.abs.UI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnitList {
    private final UI ui;
    private final float x;
    private final List<Element> elements = new ArrayList<>();
    private TransportUnit currentUnit;

    public UnitList(UI ui, float x) {
        this.ui = ui;
        this.x = x;
    }

    public void setUnits(TransportUnit[] units){
        if(currentUnit != null && !Arrays.asList(units).contains(currentUnit)) currentUnit = null;
        for (Element e : elements){
            ui.deleteElement(e);
        }
        List<TransportUnit> unitTypes = new ArrayList<>();
        for(TransportUnit unit : units){
            if(!unitTypes.contains(unit)){
                unitTypes.add(unit);
            }
        }
        float y = 7.5f;
        for (TransportUnit unit : unitTypes){
            Element button = ui.createElement(ElementType.Button, x, y, 1.2f, 1.2f, "button_background");
            button.onClick(() -> {
                if(currentUnit == null || currentUnit != unit) {
                    clearUnits();
                    currentUnit = unit;
                    setUnits(units);
                } else {
                    clearUnits();
                    currentUnit = null;
                    setUnits(units);
                }
                return null;
            });
            elements.add(button);

            Element e = ui.createElement(ElementType.Button, x, y, -1, -1, TextureMapper.join(unit.example.getTexture(),
                    unit == currentUnit ? "selected" : "normal"));
            e.setRenderInBorders(false);
            elements.add(e);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                for (IWeapon weapon : unit.create.apply(new Cell(0, 0)).getWeapons()){
                    Element e1 = ui.createElement(ElementType.Button, x + weapon.getRelativeLocation().x, y + weapon.getRelativeLocation().y, -1, -1,
                            TextureMapper.join(weapon.getTexture()));
                    e1.setRenderInBorders(false);
                    elements.add(e1);
                }
            }
            y -= 1;
        }
    }

    public TransportUnit getCurrentUnit(){
        return currentUnit;
    }

    public void clearUnits() {
        clearUnits(true);
    }

    public void clearUnits(boolean replaceCurrent){
        if(replaceCurrent)
            currentUnit = null;
        for (Element e : elements){
            ui.deleteElement(e);
        }
    }
}
