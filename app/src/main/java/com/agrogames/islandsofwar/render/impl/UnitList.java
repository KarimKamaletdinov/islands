package com.agrogames.islandsofwar.render.impl;

import com.agrogames.islandsofwar.engine.abs.transport.TransportUnit;
import com.agrogames.islandsofwar.engine.abs.weapon.Weapon;
import com.agrogames.islandsofwar.factories.WeaponFactory;
import com.agrogames.islandsofwar.types.GameObjectTypeMapper;
import com.agrogames.islandsofwar.types.TextureBitmap;
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
            Element button = ui.createElement(ElementType.Button, x, y, 1.2f, 1.2f, TextureBitmap.ButtonBackground);
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

            Element e = ui.createElement(ElementType.Button, x, y, -1, -1,
                    GameObjectTypeMapper.convert(unit.type.toRenderableObjectType(),
                            unit.equals(currentUnit)
                                    ? ObjectState.Selected
                                    : ObjectState.Picture));
            e.setRenderInBorders(false);
            elements.add(e);

            for (Weapon weapon : WeaponFactory.create(unit.type)){
                Element e1 = ui.createElement(ElementType.Button, x + weapon.getRelativeLocation().x, y + weapon.getRelativeLocation().y, -1, -1,
                        GameObjectTypeMapper.convert(weapon.getType().toRenderableObjectType(), ObjectState.Normal));
                e1.setRenderInBorders(false);
                elements.add(e1);
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
