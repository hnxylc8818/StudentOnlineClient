package com.stuonline.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.stuonline.R;

/**
 * Created by SunJiShuang on 2015/9/14.
 */
public class CustomerEditText extends RelativeLayout {
    private EditText input;
    private RelativeLayout root;
    private ImageView bt;
    private OnButtonClickListener btClickLis;
    public static final int PASSWORD = 1;
    public static final int NORMAL = 2;
    public static final int NUMBER = 3;
    private boolean isPwd;

    public CustomerEditText(Context context) {
        super(context);
        init(null);
    }

    public CustomerEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomerEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_edit, this);
        input = (EditText) findViewById(R.id.edit_edit);
        bt = (ImageView) findViewById(R.id.edit_bt);
        root = (RelativeLayout) findViewById(R.id.edit_root);
        input.setOnFocusChangeListener(onFocusChangeListener);
        input.addTextChangedListener(textWatcher);
        bt.setOnClickListener(l);
        if (attrs == null) {
            return;
        }
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomerEditText);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int index = a.getIndex(i);
            switch (index) {
                case R.styleable.CustomerEditText_text_size:
                    input.setTextSize(a.getDimensionPixelSize(index, 20));
                    break;
                case R.styleable.CustomerEditText_text_color:
                    input.setTextColor(a.getColor(index, Color.BLACK));
                    break;
                case R.styleable.CustomerEditText_hint_text:
                    input.setHint(a.getString(index));
                    break;
                case R.styleable.CustomerEditText_input_type:
                    int type = a.getInt(index, 0);
                    setInputType(type);
                    break;
                case R.styleable.CustomerEditText_button_src:
                    bt.setImageDrawable(a.getDrawable(index));
                    break;
                case R.styleable.CustomerEditText_button_visibility:
                    type = a.getInt(index, 0);
                    if (type == 1) {
                        bt.setVisibility(View.VISIBLE);
                    } else if (type == 0) {
                        bt.setVisibility(View.GONE);
                    }
                    break;
                case R.styleable.CustomerEditText_maxlenght:
                    int len = a.getInt(index, 0);
                    if (len > 0) {
                        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(len)});
                    }
                    break;
            }
        }
    }

    public void setInputType(int type) {
        if (type == PASSWORD) {
            isPwd = true;
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else if (type == NORMAL) {
            isPwd = false;
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_DATETIME_VARIATION_NORMAL);
        } else if (type == NUMBER) {
            isPwd = false;
            input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_DATETIME_VARIATION_NORMAL);
        }
    }

    private OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                root.setSelected(true);
                if (input.getText().length() > 0) {
                    bt.setVisibility(View.VISIBLE);
                }
            } else {
                root.setSelected(false);
                bt.setVisibility(View.GONE);
            }
        }
    };
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                bt.setVisibility(View.VISIBLE);
            } else {
                bt.setVisibility(View.GONE);
            }
        }
    };

    public String getText() {
        return input.getText().toString().trim();
    }

    public void setText(String text) {
        input.setText(text);
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.btClickLis = listener;
    }

    private OnClickListener l = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (btClickLis == null) {
                input.getText().clear();
            } else {
                btClickLis.onClick(input, CustomerEditText.this);
            }
        }
    };

    public interface OnButtonClickListener {
        void onClick(EditText editText, CustomerEditText v);
    }

    public boolean isPassword() {
        return isPwd;
    }

    public void setEtChangeLis(TextWatcher textWatcher) {
        input.addTextChangedListener(textWatcher);
    }
}
