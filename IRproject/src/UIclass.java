/**
 * @创建人 柳靖洋
 * @创建时间 2021/5/10
 * @描述
 */
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import org.json.*;
import java.io.*;
import java.text.CollationKey;
import java.text.Collator;

public class UIclass extends JFrame{

    public JTextField searchText;
    public JButton search;
    public JSlider jSlider;
    public JTextArea showArea;
    public ArrayList<Character> text;
    private searchMethod sm;
    public TreeMap<Character,Term> getDictionary;
    public dataProcessor data;

    public UIclass()  {
        super();
        this.setSize(700, 500);     //窗口大小
        this.getContentPane().setLayout(null);
        this.add(getJTextField(), null);
        this.add(getJButton(), null);
        this.add(getJSlider(),null);
        this.add(getJTextArea(),null);
        this.setTitle("信息检索");

    }



    private JTextField getJTextField()
    {
        if(searchText == null) {
            searchText = new javax.swing.JTextField();
            searchText.setBounds(100, 50, 300, 20);
        }
        return searchText;
    }
    private JSlider getJSlider()
    {
        if(jSlider == null)
        {
            jSlider =new JSlider();
            jSlider.setOrientation(SwingConstants.VERTICAL);
            jSlider.setBounds(500,200,20,100);
        }
    return jSlider;
    }
    private JButton getJButton()
    {
        if(search == null) {
            search = new javax.swing.JButton();
            search.setBounds(420, 50, 60, 20);
            search.setText("搜索");
        }
        return search;

    }
    private JTextArea getJTextArea()
    {
        if(showArea == null)
        {
            showArea =new JTextArea();
            showArea.setBounds(100,100,380,300);
        }
        return showArea;
    }
    public static void main(String[] args)
     {
         UIclass w = new UIclass();
         w.setVisible(true);                         //设为可见
         String text;
         text=w.searchText.getText();


     }


}
