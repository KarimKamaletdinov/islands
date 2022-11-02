package com.agrogames.islandsofwar.engine.abs.transport;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.unit.IUnit;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class TransportUnit {
    public final Function<Cell, IUnit> create;
    public final IUnit example;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public TransportUnit(Function<Cell, IUnit> create) {
        this.create = create;
        example = create.apply(new Cell(-1, -1));
    }
}
