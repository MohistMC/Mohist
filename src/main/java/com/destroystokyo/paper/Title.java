package com.destroystokyo.paper;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

/**
 * Represents a title to may be sent to a {@link Player}.
 *
 * <p>A title can be sent without subtitle text.</p>
 */
public final class Title {

    /**
     * The default number of ticks for the title to fade in.
     */
    public static final int DEFAULT_FADE_IN = 20;
    /**
     * The default number of ticks for the title to stay.
     */
    public static final int DEFAULT_STAY = 200;
    /**
     * The default number of ticks for the title to fade out.
     */
    public static final int DEFAULT_FADE_OUT = 20;

    private final BaseComponent[] title;
    private final BaseComponent[] subtitle;
    private final int fadeIn;
    private final int stay;
    private final int fadeOut;

    /**
     * Create a title with the default time values and no subtitle.
     *
     * <p>Times use default values.</p>
     *
     * @param title the main text of the title
     * @throws NullPointerException if the title is null
     */
    public Title(BaseComponent title) {
        this(title, null);
    }

    /**
     * Create a title with the default time values and no subtitle.
     *
     * <p>Times use default values.</p>
     *
     * @param title the main text of the title
     * @throws NullPointerException if the title is null
     */
    public Title(BaseComponent[] title) {
        this(title, null);
    }

    /**
     * Create a title with the default time values and no subtitle.
     *
     * <p>Times use default values.</p>
     *
     * @param title the main text of the title
     * @throws NullPointerException if the title is null
     */
    public Title(String title) {
        this(title, null);
    }

    /**
     * Create a title with the default time values.
     *
     * <p>Times use default values.</p>
     *
     * @param title    the main text of the title
     * @param subtitle the secondary text of the title
     */
    public Title(BaseComponent title, BaseComponent subtitle) {
        this(title, subtitle, DEFAULT_FADE_IN, DEFAULT_STAY, DEFAULT_FADE_OUT);
    }

    /**
     * Create a title with the default time values.
     *
     * <p>Times use default values.</p>
     *
     * @param title    the main text of the title
     * @param subtitle the secondary text of the title
     */
    public Title(BaseComponent[] title, BaseComponent[] subtitle) {
        this(title, subtitle, DEFAULT_FADE_IN, DEFAULT_STAY, DEFAULT_FADE_OUT);
    }

    /**
     * Create a title with the default time values.
     *
     * <p>Times use default values.</p>
     *
     * @param title    the main text of the title
     * @param subtitle the secondary text of the title
     */
    public Title(String title, String subtitle) {
        this(title, subtitle, DEFAULT_FADE_IN, DEFAULT_STAY, DEFAULT_FADE_OUT);
    }

    /**
     * Creates a new title.
     *
     * @param title    the main text of the title
     * @param subtitle the secondary text of the title
     * @param fadeIn   the number of ticks for the title to fade in
     * @param stay     the number of ticks for the title to stay on screen
     * @param fadeOut  the number of ticks for the title to fade out
     * @throws IllegalArgumentException if any of the times are negative
     */
    public Title(BaseComponent title, BaseComponent subtitle, int fadeIn, int stay, int fadeOut) {
        this(
                new BaseComponent[]{checkNotNull(title, "title")},
                subtitle == null ? null : new BaseComponent[]{subtitle},
                fadeIn,
                stay,
                fadeOut
        );
    }

    /**
     * Creates a new title.
     *
     * @param title    the main text of the title
     * @param subtitle the secondary text of the title
     * @param fadeIn   the number of ticks for the title to fade in
     * @param stay     the number of ticks for the title to stay on screen
     * @param fadeOut  the number of ticks for the title to fade out
     * @throws IllegalArgumentException if any of the times are negative
     */
    public Title(BaseComponent[] title, BaseComponent[] subtitle, int fadeIn, int stay, int fadeOut) {
        checkArgument(fadeIn >= 0, "Negative fadeIn: %s", fadeIn);
        checkArgument(stay >= 0, "Negative stay: %s", stay);
        checkArgument(fadeOut >= 0, "Negative fadeOut: %s", fadeOut);
        this.title = checkNotNull(title, "title");
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    /**
     * Creates a new title.
     *
     * <p>It is recommended to the {@link BaseComponent} constrctors.</p>
     *
     * @param title    the main text of the title
     * @param subtitle the secondary text of the title
     * @param fadeIn   the number of ticks for the title to fade in
     * @param stay     the number of ticks for the title to stay on screen
     * @param fadeOut  the number of ticks for the title to fade out
     */
    public Title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this(
                TextComponent.fromLegacyText(checkNotNull(title, "title")),
                subtitle == null ? null : TextComponent.fromLegacyText(subtitle),
                fadeIn,
                stay,
                fadeOut
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Gets the text of this title
     *
     * @return the text
     */
    public BaseComponent[] getTitle() {
        return this.title;
    }

    /**
     * Gets the text of this title's subtitle
     *
     * @return the text
     */
    public BaseComponent[] getSubtitle() {
        return this.subtitle;
    }

    /**
     * Gets the number of ticks to fade in.
     *
     * <p>The returned value is never negative.</p>
     *
     * @return the number of ticks to fade in
     */
    public int getFadeIn() {
        return this.fadeIn;
    }

    /**
     * Gets the number of ticks to stay.
     *
     * <p>The returned value is never negative.</p>
     *
     * @return the number of ticks to stay
     */
    public int getStay() {
        return this.stay;
    }

    /**
     * Gets the number of ticks to fade out.
     *
     * <p>The returned value is never negative.</p>
     *
     * @return the number of ticks to fade out
     */
    public int getFadeOut() {
        return this.fadeOut;
    }

    /**
     * A builder for creating titles
     */
    public static final class Builder {

        private BaseComponent[] title;
        private BaseComponent[] subtitle;
        private int fadeIn = DEFAULT_FADE_IN;
        private int stay = DEFAULT_STAY;
        private int fadeOut = DEFAULT_FADE_OUT;

        /**
         * Sets the title to the given text.
         *
         * @param title the title text
         * @return this builder instance
         * @throws NullPointerException if the title is null
         */
        public Builder title(BaseComponent title) {
            return this.title(new BaseComponent[]{checkNotNull(title, "title")});
        }

        /**
         * Sets the title to the given text.
         *
         * @param title the title text
         * @return this builder instance
         * @throws NullPointerException if the title is null
         */
        public Builder title(BaseComponent[] title) {
            this.title = checkNotNull(title, "title");
            return this;
        }

        /**
         * Sets the title to the given text.
         *
         * <p>It is recommended to the {@link BaseComponent} methods.</p>
         *
         * @param title the title text
         * @return this builder instance
         * @throws NullPointerException if the title is null
         */
        public Builder title(String title) {
            return this.title(TextComponent.fromLegacyText(checkNotNull(title, "title")));
        }

        /**
         * Sets the subtitle to the given text.
         *
         * @param subtitle the title text
         * @return this builder instance
         */
        public Builder subtitle(BaseComponent subtitle) {
            return this.subtitle(subtitle == null ? null : new BaseComponent[]{subtitle});
        }

        /**
         * Sets the subtitle to the given text.
         *
         * @param subtitle the title text
         * @return this builder instance
         */
        public Builder subtitle(BaseComponent[] subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        /**
         * Sets the subtitle to the given text.
         *
         * <p>It is recommended to the {@link BaseComponent} methods.</p>
         *
         * @param subtitle the title text
         * @return this builder instance
         */
        public Builder subtitle(String subtitle) {
            return this.subtitle(subtitle == null ? null : TextComponent.fromLegacyText(subtitle));
        }

        /**
         * Sets the number of ticks for the title to fade in
         *
         * @param fadeIn the number of ticks to fade in
         * @return this builder instance
         * @throws IllegalArgumentException if it is negative
         */
        public Builder fadeIn(int fadeIn) {
            checkArgument(fadeIn >= 0, "Negative fadeIn: %s", fadeIn);
            this.fadeIn = fadeIn;
            return this;
        }


        /**
         * Sets the number of ticks for the title to stay.
         *
         * @param stay the number of ticks to stay
         * @return this builder instance
         * @throws IllegalArgumentException if it is negative
         */
        public Builder stay(int stay) {
            checkArgument(stay >= 0, "Negative stay: %s", stay);
            this.stay = stay;
            return this;
        }

        /**
         * Sets the number of ticks for the title to fade out.
         *
         * @param fadeOut the number of ticks to fade out
         * @return this builder instance
         * @throws IllegalArgumentException if it is negative
         */
        public Builder fadeOut(int fadeOut) {
            checkArgument(fadeOut >= 0, "Negative fadeOut: %s", fadeOut);
            this.fadeOut = fadeOut;
            return this;
        }

        /**
         * Create a title based on the values in the builder.
         *
         * @return a title from the values in this builder
         * @throws IllegalStateException if title isn't specified
         */
        public Title build() {
            checkState(title != null, "Title not specified");
            return new Title(this.title, this.subtitle, this.fadeIn, this.stay, this.fadeOut);
        }
    }
}
