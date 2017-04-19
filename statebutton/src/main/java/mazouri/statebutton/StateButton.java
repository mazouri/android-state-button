package mazouri.statebutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by wangdongdong on 17-3-27.
 */

public class StateButton extends RelativeLayout {

    public static final int ID_ICON =0x1000001;
    public static final int ID_PROGRESS =0x1000002;
    public static final int ID_FRAME = 0x1000003;
    private TextView mTextView;
    private ImageView mIconView;

    /**
     * Texts for the different states
     */
    private String disabledText = "";
    private String enabledText = "";
    private String selectedText = "";
    private String loadingText = "";

    /**
     * Texts color for the different states
     */
    private int disabledTextColor;
    private int enabledTextColor;
    private int selectedTextColor;
    private int loadingTextColor;

    /**
     * Backgrounds for different states
     */
    private int disabledBackground;
    private int enabledBackground;
    private int selectedBackground;
    private int loadingBackground;

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
     * 进度条的颜色。
     */
    private int progressLineColor = Color.BLUE;

    private int progressTextColor;

    /**
     * 进度条的宽度。
     */
    private int progressLineWidth = 8;

    /**
     * 进度更新时间间隔
     */
    private int progressChangeDuration = 0;

    private int maxProgress = 100;

    /**
     * Previous state of button
     */
    private BUTTON_STATES oldState;

    /**
     * Current state of the button
     */
    private BUTTON_STATES currentState;
    private CircleTextProgressbar mCircleTextProgressbar;
    private FrameLayout mFrameLayout;

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
        loadingText = a.getString(R.styleable.StateButton_loadingText);

        // Texts color
        disabledTextColor = a.getColor(R.styleable.StateButton_disabledTextColor, Color.WHITE);
        enabledTextColor = a.getColor(R.styleable.StateButton_enabledTextColor, Color.WHITE);
        selectedTextColor = a.getColor(R.styleable.StateButton_selectedTextColor, Color.WHITE);
        loadingTextColor = a.getColor(R.styleable.StateButton_loadingTextColor, Color.WHITE);

        // Backgrounds
        disabledBackground = a.getResourceId(R.styleable.StateButton_disabledBackground, 0);
        enabledBackground = a.getResourceId(R.styleable.StateButton_enabledBackground, 0);
        selectedBackground = a.getResourceId(R.styleable.StateButton_selectedBackground, 0);
        loadingBackground = a.getResourceId(R.styleable.StateButton_loadingBackground, 0);

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

        progressLineColor = a.getColor(R.styleable.StateButton_barProgressLineColor, Color.TRANSPARENT);
        progressTextColor = a.getColor(R.styleable.StateButton_barProgressTextColor, Color.WHITE);
        progressLineWidth = a.getDimensionPixelSize(R.styleable.StateButton_barProgressLineWidth, 8);
        progressChangeDuration = a.getInt(R.styleable.StateButton_barProgressChangeDuration, 0);
        maxProgress = a.getInt(R.styleable.StateButton_barMaxProgress, 100);

        a.recycle();

        oldState = currentState;

        mIconView = new ImageView(context, attrs);
        LayoutParams lpIcon = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lpIcon.width = iconWidth;
        lpIcon.height = iconHeight;
        mIconView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mIconView.setId(ID_ICON);
//        addView(mIconView, lpIcon);

        mCircleTextProgressbar = new CircleTextProgressbar(context, attrs);
        LayoutParams lpProgressbar = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        lpProgressbar.width = iconWidth;
        lpProgressbar.height = iconHeight;
        mCircleTextProgressbar.setId(ID_PROGRESS);
        mCircleTextProgressbar.setProgressColor(progressLineColor);
        mCircleTextProgressbar.setProgressLineWidth(progressLineWidth);
        mCircleTextProgressbar.setProgressChangeDuration(progressChangeDuration);
        mCircleTextProgressbar.setMaxProgress(maxProgress);
        mCircleTextProgressbar.setProgressType(CircleTextProgressbar.ProgressType.COUNT_BACK);
        mCircleTextProgressbar.setVisibility(GONE);

        mFrameLayout = new FrameLayout(context, attrs);
        LayoutParams lpFrame = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lpFrame.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        lpFrame.topMargin = iconTopMargin;
        mFrameLayout.addView(mIconView, lpIcon);
        mFrameLayout.addView(mCircleTextProgressbar, lpProgressbar);
        mFrameLayout.setId(ID_FRAME);
        addView(mFrameLayout, lpFrame);

        mTextView = new TextView(context, attrs);
        LayoutParams lpText = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lpText.topMargin = drawablePadding;
        lpText.addRule(RelativeLayout.BELOW, ID_FRAME);
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
            case 3: //loading
                attrWrapper.text = loadingText;
                attrWrapper.textColor = loadingTextColor;
                attrWrapper.background = loadingBackground;
                attrWrapper.icon = 0;
                attrWrapper.iconVisibility = GONE;
                attrWrapper.clickable = false;
                break;
        }

        if (attrWrapper.background != 0) {
            setBackgroundResource(attrWrapper.background);
        }

        if (attrWrapper.icon != 0) {
            mFrameLayout.setVisibility(VISIBLE);
            mIconView.setImageResource(attrWrapper.icon);
        } else {
            mFrameLayout.setVisibility(GONE);
        }

        if (!TextUtils.isEmpty(attrWrapper.text)) {
            mTextView.setVisibility(VISIBLE);
            mTextView.setTextColor(attrWrapper.textColor);
            mTextView.setText(attrWrapper.text);
        } else {
            mTextView.setVisibility(GONE);
        }

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
        if (state == BUTTON_STATES.LOADING) {
            mCircleTextProgressbar.setVisibility(VISIBLE);
            mIconView.setVisibility(GONE);
            mCircleTextProgressbar.reStart();
        } else {
            mCircleTextProgressbar.setVisibility(GONE);
            mIconView.setVisibility(VISIBLE);
            setup();
        }
    }

    public void resetStateFromLoading() {
        setState(oldState);
    }

    public void setProgressText(String progressText) {
        mCircleTextProgressbar.setText(progressText);
        mCircleTextProgressbar.setTextColor(progressTextColor);
    }

    public void setCountdownProgressListener(final OnCountdownListener onCountdownListener) {
        mCircleTextProgressbar.setCountdownProgressListener(0, new CircleTextProgressbar.OnCountdownProgressListener() {
            @Override
            public void onProgress(int what, int progress) {
                onCountdownListener.onProgress(progress);
                if (progress == 0) {
                    resetStateFromLoading();
                }
            }
        });
    }

    /**
     * 进度监听。
     */
    public interface OnCountdownListener {

        /**
         * 进度通知。
         *
         * @param progress 进度值。
         */
        void onProgress(int progress);
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
        SELECTED(2),
        LOADING(3);

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
