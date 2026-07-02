package interfaz.componentes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.function.Consumer;

import java.util.HashSet;
import java.util.Set;

public class DatePickerPanel extends JPanel {
    private YearMonth currentMonth;
    private LocalDate selectedDate;
    private Set<LocalDate> multiSelectedDates = new HashSet<>();
    private boolean isMultiSelect = false;
    private Consumer<LocalDate> onDateSelected;
    private JPanel calendarGrid;
    private JLabel lblMonth;

    public DatePickerPanel(Consumer<LocalDate> onDateSelected) {
        this(onDateSelected, false);
    }

    public DatePickerPanel(Consumer<LocalDate> onDateSelected, boolean isMultiSelect) {
        this.onDateSelected = onDateSelected;
        this.isMultiSelect = isMultiSelect;
        this.currentMonth = YearMonth.now();
        this.selectedDate = null;
        setOpaque(false);
        setLayout(new BorderLayout(0, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(280, 260));
        construir();
    }

    private void construir() {
        removeAll();

        // Header con navegación de meses
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JButton btnPrev = crearNavBtn("<");
        btnPrev.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            construir();
        });

        JButton btnNext = crearNavBtn(">");
        btnNext.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            construir();
        });

        String monthName = currentMonth.getMonth().getDisplayName(TextStyle.FULL, new Locale("es")) + " " + currentMonth.getYear();
        lblMonth = new JLabel(monthName.substring(0, 1).toUpperCase() + monthName.substring(1), SwingConstants.CENTER);
        lblMonth.setFont(Tema.FONT_BOLD);
        lblMonth.setForeground(Tema.TEXTO_PRINCIPAL);

        header.add(btnPrev, BorderLayout.WEST);
        header.add(lblMonth, BorderLayout.CENTER);
        header.add(btnNext, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Grid del calendario
        calendarGrid = new JPanel(new GridLayout(0, 7, 2, 2));
        calendarGrid.setOpaque(false);

        // Días de la semana
        String[] dias = {"Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Do"};
        for (String d : dias) {
            JLabel lbl = new JLabel(d, SwingConstants.CENTER);
            lbl.setFont(Tema.FONT_PEQUENA);
            lbl.setForeground(Tema.TEXTO_SECUNDARIO);
            calendarGrid.add(lbl);
        }

        // Días del mes
        LocalDate first = currentMonth.atDay(1);
        int dayOfWeek = first.getDayOfWeek().getValue(); // 1=Lunes

        // Espacios vacíos antes del primer día
        for (int i = 1; i < dayOfWeek; i++) {
            calendarGrid.add(new JLabel(""));
        }

        int daysInMonth = currentMonth.lengthOfMonth();
        LocalDate today = LocalDate.now();

        for (int day = 1; day <= daysInMonth; day++) {
            final LocalDate date = currentMonth.atDay(day);
            JLabel dayLabel = new JLabel(String.valueOf(day), SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    if ((isMultiSelect && multiSelectedDates.contains(date)) || (!isMultiSelect && date.equals(selectedDate))) {
                        g2.setColor(Tema.PRIMARIO);
                        g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 8, 8);
                    } else if (date.equals(today)) {
                        g2.setColor(Tema.withAlpha(Tema.PRIMARIO, 40));
                        g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 8, 8);
                    }
                    g2.dispose();
                    super.paintComponent(g);
                }
            };

            dayLabel.setFont(Tema.FONT_REGULAR);
            dayLabel.setOpaque(false);
            dayLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            dayLabel.setPreferredSize(new Dimension(32, 30));

            if ((isMultiSelect && multiSelectedDates.contains(date)) || (!isMultiSelect && date.equals(selectedDate))) {
                dayLabel.setForeground(Color.WHITE);
            } else if (date.equals(today)) {
                dayLabel.setForeground(Tema.PRIMARIO);
            } else if (date.isBefore(today)) {
                dayLabel.setForeground(Tema.withAlpha(Tema.TEXTO_SECUNDARIO, 100));
            } else {
                dayLabel.setForeground(Tema.TEXTO_PRINCIPAL);
            }

            dayLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (isMultiSelect) {
                        if (multiSelectedDates.contains(date)) {
                            multiSelectedDates.remove(date);
                        } else {
                            multiSelectedDates.add(date);
                        }
                    } else {
                        selectedDate = date;
                    }
                    
                    if (onDateSelected != null) {
                        onDateSelected.accept(date);
                    }
                    construir();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    boolean isSel = isMultiSelect ? multiSelectedDates.contains(date) : date.equals(selectedDate);
                    if (!isSel) {
                        dayLabel.setForeground(Tema.PRIMARIO);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    boolean isSel = isMultiSelect ? multiSelectedDates.contains(date) : date.equals(selectedDate);
                    if (!isSel) {
                        if (date.equals(today)) {
                            dayLabel.setForeground(Tema.PRIMARIO);
                        } else if (date.isBefore(today)) {
                            dayLabel.setForeground(Tema.withAlpha(Tema.TEXTO_SECUNDARIO, 100));
                        } else {
                            dayLabel.setForeground(Tema.TEXTO_PRINCIPAL);
                        }
                    }
                }
            });

            calendarGrid.add(dayLabel);
        }

        add(calendarGrid, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JButton crearNavBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(Tema.FONT_BOLD);
        btn.setForeground(Tema.PRIMARIO);
        btn.setBackground(Tema.FONDO);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(40, 30));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setForeground(Tema.SECUNDARIO); }
            @Override public void mouseExited(MouseEvent e) { btn.setForeground(Tema.PRIMARIO); }
        });
        return btn;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public Set<LocalDate> getMultiSelectedDates() {
        return multiSelectedDates;
    }

    public void setSelectedDate(LocalDate date) {
        this.selectedDate = date;
        if (date != null) {
            this.currentMonth = YearMonth.of(date.getYear(), date.getMonth());
            if (isMultiSelect) {
                multiSelectedDates.clear();
                multiSelectedDates.add(date);
            }
        }
        construir();
    }
}
