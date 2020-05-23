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

package modaldialog;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/**
*
* The dialog that's shown when user wants to change the size of current palette.
*
**/

public class TMPaletteSizeDialog extends TMModalDialog {

    private JLabel sizeLabel;
    private JTextField sizeField;

/**
*
* Creates the Palette Size dialog.
*
**/

    public TMPaletteSizeDialog(Frame owner, utils.Xlator xl) {
        super(owner, "Palette_Size_Dialog_Title", xl);
    }

/**
*
* Gets the palette size given by the user.
*
**/

    public int getPaletteSize() {
        return Integer.parseInt(sizeField.getText());
    }

/**
*
*
*
**/

    protected JPanel getDialogPane() {
        JPanel p = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        p.setLayout(gbl);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        sizeLabel = new JLabel(xlate("Size_Prompt"));
        buildConstraints(gbc, 0, 0, 1, 1, 50, 100);
        gbl.setConstraints(sizeLabel, gbc);
        p.add(sizeLabel);
        sizeField = new JTextField();
        buildConstraints(gbc, 1, 0, 1, 1, 50, 100);
        gbl.setConstraints(sizeField, gbc);
        p.add(sizeField);
        p.setPreferredSize(new Dimension(200, 50));

        sizeField.setColumns(4);
        sizeField.addKeyListener(new DecimalNumberVerifier());
        sizeField.getDocument().addDocumentListener(new TMDocumentListener());

        return p;
    }

    public int showDialog(int initialSize) {
        sizeField.setText(Integer.toString(initialSize));
        maybeEnableOKButton();
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                sizeField.requestFocus();
            }
        });
        return super.showDialog();
    }

    public boolean inputOK() {
        return !(sizeField.getText().equals("") || (getPaletteSize() == 0));
    }

}