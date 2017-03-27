package mazouri.statebutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by wangdongdong on 17-3-27.
 */

public class StateButton extends RelativeLayout {

    public static final int ID_ICON =0x1000001;
    private TextView mTextView;
    private ImageView mIconView;

    /**
     * Texts for the different states
     */
    private String disabledText = "";
    private String enabledText = "";
    private String selectedText = "";

    /**
     * Texts color for the different states
     */
    private int disabledTextColor;
    private int enabledTextColor;
    private int selectedTextColor;

    /**
     * Backgrounds for different states
     */
    private int disabledBackground;
    private int enabledBackground;
    private int selectedBackground;

    /**
     * Icons color for different states
     */
    private int disabledIcon;
    private int enabledIcon;
    private int selectedIcon;

    /**
     * Icons visibility for different states
     */
    private boolean disabledIconVisibility;
    private boolean enabledIconVisibility;
    private boolean selectedIconVisibility;

    /**
     * Previous state of button
     */
    private BUTTON_STATES oldState;

    /**
     * Current state of the button
     */
    private BUTTON_STATES currentState;

    public StateButton(Context context) {
        this(context, null);
    }

    public StateButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.StateButton, defStyleAttr, 0);

        currentState = BUTTON_STATES.fromValue(a.getInt(R.styleable.StateButton_state, 1));
        // Texts
        disabledText = a.getString(R.styleable.StateButton_disabledText);
        enabledText = a.getString(R.styleable.StateButton_enabledText);
        selectedText = a.getString(R.styleable.StateButton_selectedText);

        // Texts color
        disabledTextColor = a.getColor(R.styleable.StateButton_disabledTextColor, Color.WHITE);
        enabledTextColor = a.getColor(R.styleable.StateButton_enabledTextColor, Color.WHITE);
        selectedTextColor = a.getColor(R.styleable.StateButton_selectedTextColor, Color.WHITE);

        // Backgrounds
        disabledBackground = a.getResourceId(R.styleable.StateButton_disabledBackground, 0);
        enabledBackground = a.getResourceId(R.styleable.StateButton_enabledBackground, 0);
        selectedBackground = a.getResourceId(R.styleable.StateButton_selectedBackground, 0);

        // Icons
        disabledIcon = a.getResourceId(R.styleable.StateButton_disabledIcon, 0);
        enabledIcon = a.getResourceId(R.styleable.StateButton_enabledIcon, 0);
        selectedIcon = a.getResourceId(R.styleable.StateButton_selectedIcon, 0);
        // Icons visibility
        disabledIconVisibility = a.getBoolean(R.styleable.StateButton_disabledIconVisible, true);
        enabledIconVisibility = a.getBoolean(R.styleable.StateButton_enabledIconVisible, true);
        selectedIconVisibility = a.getBoolean(R.styleable.StateButton_selectedIconVisible, true);

        int textSize = a.getDimensionPixelSize(R.styleable.StateButton_textSize, 0);
        int textStyle = a.getInt(R.styleable.StateButton_textStyle, 0);

        int iconTopMargin = a.getDimensionPixelSize(R.styleable.StateButton_iconTopMargin, dip2px(18f));
        int iconWidth = a.getDimensionPixelSize(R.styleable.StateButton_iconWidth, dip2px(30f));
        int iconHeight = a.getDimensionPixelSize(R.styleable.StateButton_iconHeight, dip2px(30f));
        int drawablePadding = a.getDimensionPixelSize(R.styleable.StateButton_drawablePadding, dip2px(10f));

        a.recycle();

        oldState = currentState;

//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View viewRoot = inflater.inflate(R.layout.statebutton_layout, this, true);
//        mIconView = (ImageView) viewRoot.findViewById(R.id.icon);
//        mTextView = (TextView) viewRoot.findViewById(R.id.text_view);

        mIconView = new ImageView(context, attrs);
        LayoutParams lpIcon = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lpIcon.topMargin = iconTopMargin;
        lpIcon.width = iconWidth;
        lpIcon.height = iconHeight;
        lpIcon.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        mIconView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mIconView.setId(ID_ICON);
        addView(mIconView, lpIcon);

        mTextView = new TextView(context, attrs);
        LayoutParams lpText = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lpText.topMargin = drawablePadding;
        lpText.addRule(RelativeLayout.BELOW, ID_ICON);
        lpText.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);

        mTextView.setDuplicateParentStateEnabled(true);
        mTextView.setClickable(false);
        if (textSize > 0) {
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }

        Typeface tf = Typeface.defaultFromStyle(textStyle);
        mTextView.setTypeface(tf);
        addView(mTextView, lpText);

        setup();
    }

    private class AttrWrapper {
        String text;
        int textColor;
        int background;
        int icon;
        int iconVisibility;
        boolean clickable;
    }

    private void setup() {
        final AttrWrapper attrWrapper = new AttrWrapper();

        switch (currentState.getValue()) {
            case 0: // disabled
                attrWrapper.text = disabledText;
                attrWrapper.textColor = disabledTextColor;
                attrWrapper.background = disabledBackground;
                attrWrapper.icon = disabledIcon;
                attrWrapper.iconVisibility = disabledIconVisibility ? VISIBLE : INVISIBLE;
                attrWrapper.clickable = false;
                break;
            case 1: // enabled
                attrWrapper.text = enabledText;
                attrWrapper.textColor = enabledTextColor;
                attrWrapper.background = enabledBackground;
                attrWrapper.icon = enabledIcon;
                attrWrapper.iconVisibility = enabledIconVisibility ? VISIBLE : INVISIBLE;
                attrWrapper.clickable = true;
                break;
            case 2: // selected
                attrWrapper.text = selectedText;
                attrWrapper.textColor = selectedTextColor;
                attrWrapper.background = selectedBackground;
                attrWrapper.icon = selectedIcon;
                attrWrapper.iconVisibility = selectedIconVisibility ? VISIBLE : INVISIBLE;
                attrWrapper.clickable = true;
                break;
        }

        if (attrWrapper.background != 0) {
            setBackgroundResource(attrWrapper.background);
        }

        if (attrWrapper.icon != 0) {
            mIconView.setImageResource(attrWrapper.icon);
        }

        mTextView.setTextColor(attrWrapper.textColor);
        mTextView.setText(attrWrapper.text);

        setClickable(attrWrapper.clickable);
        invalidate();
    }

    public String getDisabledText() {
        return disabledText;
    }

    public StateButton setDisabledText(String disabledText) {
        this.disabledText = disabledText;
        return this;
    }

    public String getEnabledText() {
        return enabledText;
    }

    public StateButton setEnabledText(String enabledText) {
        this.enabledText = enabledText;
        return this;
    }

    public String getSelectedText() {
        return selectedText;
    }

    public StateButton setSelectedText(String selectedText) {
        this.selectedText = selectedText;
        return this;
    }

    public int getDisabledTextColor() {
        return disabledTextColor;
    }

    public StateButton setDisabledTextColor(int disabledTextColor) {
        this.disabledTextColor = disabledTextColor;
        return this;
    }

    public int getEnabledTextColor() {
        return enabledTextColor;
    }

    public StateButton setEnabledTextColor(int enabledTextColor) {
        this.enabledTextColor = enabledTextColor;
        return this;
    }

    public int getSelectedTextColor() {
        return selectedTextColor;
    }

    public StateButton setSelectedTextColor(int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
        return this;
    }

    public int getDisabledBackground() {
        return disabledBackground;
    }

    public StateButton setDisabledBackground(int disabledBackground) {
        this.disabledBackground = disabledBackground;
        return this;
    }

    public int getEnabledBackground() {
        return enabledBackground;
    }

    public StateButton setEnabledBackground(int enabledBackground) {
        this.enabledBackground = enabledBackground;
        return this;
    }

    public int getSelectedBackground() {
        return selectedBackground;
    }

    public StateButton setSelectedBackground(int selectedBackground) {
        this.selectedBackground = selectedBackground;
        return this;
    }

    public int getDisabledIcon() {
        return disabledIcon;
    }

    public StateButton setDisabledIcon(int disabledIcon) {
        this.disabledIcon = disabledIcon;
        return this;
    }

    public int getEnabledIcon() {
        return enabledIcon;
    }

    public StateButton setEnabledIcon(int enabledIcon) {
        this.enabledIcon = enabledIcon;
        return this;
    }

    public int getSelectedIcon() {
        return selectedIcon;
    }

    public StateButton setSelectedIcon(int selectedIcon) {
        this.selectedIcon = selectedIcon;
        return this;
    }

    public boolean isDisabledIconVisibility() {
        return disabledIconVisibility;
    }

    public StateButton setDisabledIconVisibility(boolean disabledIconVisibility) {
        this.disabledIconVisibility = disabledIconVisibility;
        return this;
    }

    public boolean isEnabledIconVisibility() {
        return enabledIconVisibility;
    }

    public StateButton setEnabledIconVisibility(boolean enabledIconVisibility) {
        this.enabledIconVisibility = enabledIconVisibility;
        return this;
    }

    public boolean isSelectedIconVisibility() {
        return selectedIconVisibility;
    }

    public StateButton setSelectedIconVisibility(boolean selectedIconVisibility) {
        this.selectedIconVisibility = selectedIconVisibility;
        return this;
    }

    public void refresh() {
        invalidate();
    }

    public int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * Set the state of the button
     *
     * @param state
     */
    public void setState(BUTTON_STATES state) {
        this.oldState = this.currentState;
        this.currentState = state;
        setup();
    }

    /**
     * Get the buttons state
     *
     * @return
     */
    public BUTTON_STATES getState() {
        return currentState;
    }

    /**
     * Enum for states
     */
    public enum BUTTON_STATES {

        DISABLED(0),
        ENABLED(1),
        SELECTED(2);

        private final int value;

        BUTTON_STATES(final int newValue) {
            value = newValue;
        }

        public int getValue() {
            return value;
        }

        public static BUTTON_STATES fromValue(int value) {
            for (BUTTON_STATES state : BUTTON_STATES.values()) {
                if (state.value == value) {
                    return state;
                }
            }
            return null;
        }
    }
}
