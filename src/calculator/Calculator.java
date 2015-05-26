package calculator;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Calculator {

    static final class Screen extends JFrame {

        //Swing Components
        JButton[] buttons = new JButton[15];
        JButton equalButton = new JButton("=");
        JButton modeButton = new JButton("RPN");
        JButton decimalButton = new JButton(".");
        JButton clearButton = new JButton("C");
        JTextArea display = new JTextArea(1, 15);
        JPanel panel = new JPanel();
        JPanel buttonPanel = new JPanel();

        //Font size and type
        Font font = new Font("Verdana", Font.PLAIN, 14);

        //Logic variables
        double num = 0;
        double ans = 0;
        String opr = "";
        int exponent = 0;
        boolean decimal = false;
        boolean rpnEnter = true;
        String mode = "Norm";

        Screen() {
            //Set text display settings
            display.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            display.setFont(font);
            display.setDisabledTextColor(Color.BLACK); //change text color
            display.setEnabled(false); //make read only text displayed
            display.setBackground(Color.WHITE);
            display.setText("0");
            panel.add(display);

            //Set Button text and add to frame
            clearButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    num = 0;
                    ans = 0;
                    opr = "";
                    decimal = false;
                    exponent = 0;
                    rpnEnter = false;
                    display.setText("" + num);
                }
            });
            decimalButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!decimal) {
                        decimal = true;
                        exponent = 0;
                    }
                }
            });
            modeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (mode) {
                        case "Norm":
                            mode = "RPN";
                            modeButton.setText("Norm");
                            break;
                        case "RPN":
                            mode = "Norm";
                            modeButton.setText("RPN");
                            break;
                    }
                    num = 0;
                    ans = 0;
                    opr = "";
                    decimal = false;
                    exponent = 0;
                    rpnEnter = false;
                    display.setText("" + num);
                }
            });
            buttonPanel.setLayout(new GridLayout(5, 5));
            for (int i = 0; i < buttons.length; i++) {
                buttons[i] = new JButton("" + i);
                setActionNumber(buttons[i], i);
                if (i == 10) {
                    buttons[i].setText("X");
                }
                if (i == 11) {
                    buttons[i].setText("/");
                }
                if (i == 12) {
                    buttons[i].setText("-");
                }
                if (i == 13) {
                    buttons[i].setText("+");
                }
                if (i == 14) {
                    buttons[i].setText("= / E");
                }
                buttonPanel.add(buttons[i]);
            }
            panel.add(buttonPanel);
            panel.add(clearButton);
            panel.add(modeButton);
            panel.add(decimalButton);
            add(panel);
        }

        //Add action listeners to all buttons
        void setActionNumber(JButton button, int i) {
            final int j = i;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (mode) {
                        case "Norm":
                            //Add Normal Mode button operations
                            //Number Keys
                            if (num == 0 && j < 10) {
                                display.setText("");
                                if (!decimal) {
                                    num = num * 10 + j;
                                } else {
                                    exponent--;
                                    num = num + (j * Math.pow(10, exponent));
                                }
                                display.setText("" + num);
                            } else if (j < 10) {
                                display.setText("");
                                if (!decimal) {
                                    num = num * 10 + j;
                                } else {
                                    exponent--;
                                    num = num + (j * Math.pow(10, exponent));
                                }
                                display.setText("" + num);
                            } //Operation Keys
                            else if (j > 9) {
                                decimal = false;
                                if (opr.equals("")) {
                                    ans = num;
                                    num = 0;
                                    setOPR(j);
                                } else {
                                    display.setText("" + apply(num, j));
                                    setOPR(j);
                                    num = 0;
                                }
                            }
                            break;
                        case "RPN": {
                            //Add RPN Mode button operations
                            //Number Keys
                            if (j < 10) {
                                if (rpnEnter) {
                                    if (!decimal) {
                                        num = num * 10 + j;
                                    } else {
                                        exponent--;
                                        num = num + (j * Math.pow(10, exponent));
                                    }
                                    display.setText("" + num);
                                } else {
                                    if (!decimal) {
                                        ans = ans * 10 + j;
                                    } else {
                                        exponent--;
                                        ans = ans + (j * Math.pow(10, exponent));
                                    }
                                    display.setText("" + ans);
                                }
                            } //Operation Keys
                            else if (j > 9) {
                                if (j == 14) { //Equals key
                                    if (mode.equals("Norm")) {
                                        display.setText("" + ans);
                                    }
                                    exponent = 0;
                                    decimal = false;
                                    rpnEnter = true;
                                } else { //operation keys
                                    if (rpnEnter) {
                                        setOPR(j);
                                        display.setText("" + apply(num, j));
                                        num = 0;
                                    }
                                }
                            }
                        }
                        break;
                    }
                }

                //Pass the index to this function to fulfill the operation
                double apply(double num, int j) {
                    switch (opr) {
                        case "mult": { //multiply
                            ans = ans * num;
                            break;
                        }
                        case "div": {//divide
                            ans = ans / num;
                            break;
                        }
                        case "sub": {//subtract
                            ans = ans - num;
                            break;
                        }
                        case "add": {//add
                            ans = ans + num;
                            break;
                        }
                        case "equal": {//equals
                            if (mode.equals("Norm")) {
                                display.setText("" + ans);
                            }
                            rpnEnter = true;
                            break;
                        }
                    }
                    num = 0;
                    return ans;
                }

                //Pass index to this function to set the operation variable
                private void setOPR(int j) {
                    switch (j) {
                        case 10: { //multiply
                            opr = "mult";
                            break;
                        }
                        case 11: {//divide
                            opr = "div";
                            break;
                        }
                        case 12: {//subtract
                            opr = "sub";
                            break;
                        }
                        case 13: {//add
                            opr = "add";
                            break;
                        }
                        case 14: {//equals
                            opr = "equal";
                            break;
                        }
                    }
                }
            });
        }
    }

    public static void main(String[] args) {
        Screen screen = new Screen();
        screen.setVisible(true);
        //Set operations for created JFrame
        screen.setTitle("Calculator"); //set top titlebar
        screen.setSize(300, 250); //set frame size
        screen.setResizable(false); //not allow user to resize window
        screen.setLocationRelativeTo(null); //start window in the center of screen
        screen.setDefaultCloseOperation(EXIT_ON_CLOSE); //program will exit if close is clicked on window
    }

}
