/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.client.loading;


/**
 * This is an implementation of the LoadingOverlay that calls back into the early window rendering, as part of the
 * game loading cycle. We completely replace the {@link #render(GuiGraphics, int, int, float)} call from the parent
 * with one of our own, that allows us to blend our early loading screen into the main window, in the same manner as
 * the Mojang screen. It also allows us to see and tick appropriately as the later stages of the loading system run.
 *
 * It is somewhat a copy of the superclass render method.
 */
public class ForgeLoadingOverlay {
}
