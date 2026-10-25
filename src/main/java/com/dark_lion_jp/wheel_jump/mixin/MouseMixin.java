package com.dark_lion_jp.wheel_jump.mixin;

import java.lang.Math;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseMixin {

  // Inject at the beginning of the onScroll method to override its behavior.
  @Inject(method = "onScroll", at = @At("HEAD"))
  private void onScroll(long window, double horizontal, double vertical, CallbackInfo callbackInfo) {
    if (vertical == 0) {
      return;
    }

    Minecraft client = Minecraft.getInstance();
    if (
        client == null ||
        client.player == null ||
        client.gui.screen() != null
    ) {
      return;
    }

    Inventory inventory = client.player.getInventory();
    if (inventory == null) {
      return;
    }

    int currentSlot = inventory.getSelectedSlot();

    // Determine scroll direction: +1 for down, -1 for up
    int delta = (int) Math.signum(vertical);

    // Update selected hotbar slot with wrapping (0–8)
    inventory.setSelectedSlot(((currentSlot + delta) % 9 + 9) % 9);

    // Jump if the player is on the ground and no GUI screen is open
    if (client.player.onGround()) {
      client.player.jumpFromGround();
    }
  }
}
