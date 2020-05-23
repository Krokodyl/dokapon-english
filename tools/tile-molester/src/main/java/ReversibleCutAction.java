/*
*
*    Copyright (C) 2003 Kent Hansen.
*
*    This file is part of Tile Molester.
*
*    Tile Molester is free software; you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation; either version 2 of the License, or
*    (at your option) any later version.
*
*    Tile Molester is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*/

/**
*
* Allows undo/redo for the Cut operation.
*
**/

public class ReversibleCutAction extends ReversibleAction {

    private TMSelectionCanvas selection;
    private TMEditorCanvas owner;
    private int x, y;

    public ReversibleCutAction(TMSelectionCanvas selection, TMEditorCanvas owner) {
        super("Cut");   // i18n
        this.selection = selection;
        this.owner = owner;
        x = selection.getX() / selection.getScaledTileDim();
        y = selection.getY() / selection.getScaledTileDim();
    }

    public void undo() {
        owner.showSelection(selection, x, y);
    }

    public void redo() {
        owner.remove(selection);
        owner.repaint();
    }

    public boolean canUndo() { return true; }
    public boolean canRedo() { return true; }

}