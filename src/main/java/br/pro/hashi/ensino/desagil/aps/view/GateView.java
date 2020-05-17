// FONTE DAS IMAGENS: https://en.wikipedia.org/wiki/Logic_gate (domínio público)

package br.pro.hashi.ensino.desagil.aps.view;

import br.pro.hashi.ensino.desagil.aps.model.Gate;
import br.pro.hashi.ensino.desagil.aps.model.Light;
import br.pro.hashi.ensino.desagil.aps.model.Switch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

public class GateView extends FixedPanel implements ItemListener, MouseListener {
    private static final int BORDER = 10;
    private static final int SWITCH_SIZE = 18;
    private static final int GATE_WIDTH = 90;
    private static final int GATE_HEIGHT = 60;
    private static final int LIGHT_SIZE = 18;

    private final Switch[] switches;
    private final Gate gate;
    private final Light light;
    private final JCheckBox[] inputBoxes;
    private final Image image;

    public GateView(Gate gate) {
        super(BORDER + SWITCH_SIZE + GATE_WIDTH + LIGHT_SIZE + BORDER, GATE_HEIGHT);

        this.gate = gate;

        int inputSize = gate.getInputSize();

        switches = new Switch[inputSize];
        inputBoxes = new JCheckBox[inputSize];

        for (int i = 0; i < inputSize; i++) {
            switches[i] = new Switch();
            inputBoxes[i] = new JCheckBox();

            gate.connect(i, switches[i]);
        }

        light = new Light(255, 0, 0);
        light.connect(0, gate);

        int y = -(SWITCH_SIZE / 2);
        int step = (GATE_HEIGHT / (inputSize + 1));
        for (JCheckBox inputBox : inputBoxes) {
            y += step;
            add(inputBox, BORDER, y, SWITCH_SIZE, SWITCH_SIZE);
        }

        String name = gate.toString() + ".png";
        URL url = getClass().getClassLoader().getResource(name);
        image = getToolkit().getImage(url);

        addMouseListener(this);

        for (JCheckBox inputBox : inputBoxes) {
            inputBox.addItemListener(this);
        }

        update();
    }

    private void update() {
        for (int i = 0; i < gate.getInputSize(); i++) {
            if (inputBoxes[i].isSelected()) {
                switches[i].turnOn();
            } else {
                switches[i].turnOff();
            }
        }

        repaint();
    }

    @Override
    public void itemStateChanged(ItemEvent event) {
        update();
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        int x = event.getX();
        int y = event.getY();

        int r = LIGHT_SIZE / 2;
        int cx = BORDER + SWITCH_SIZE + GATE_WIDTH + r;
        int cy = GATE_HEIGHT / 2;

        int dx = cx - x;
        int dy = cy - y;

        if (dx * dx + dy * dy < r * r) {
            Color oldColor = light.getColor();
            Color newColor = JColorChooser.showDialog(this, null, oldColor);

            if (newColor != null) {
                light.setColor(newColor);
            }

            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {
    }

    @Override
    public void mouseReleased(MouseEvent event) {
    }

    @Override
    public void mouseEntered(MouseEvent event) {
    }

    @Override
    public void mouseExited(MouseEvent event) {
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(image, BORDER + SWITCH_SIZE, 0, GATE_WIDTH, GATE_HEIGHT, this);

        g.setColor(light.getColor());
        g.fillOval(BORDER + SWITCH_SIZE + GATE_WIDTH, (GATE_HEIGHT - LIGHT_SIZE) / 2, LIGHT_SIZE, LIGHT_SIZE);

        getToolkit().sync();
    }
}
