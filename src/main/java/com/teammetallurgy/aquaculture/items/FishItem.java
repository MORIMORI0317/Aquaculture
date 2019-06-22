package com.teammetallurgy.aquaculture.items;

import com.google.common.base.Preconditions;
import com.teammetallurgy.aquaculture.Aquaculture;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class FishItem extends Item {
    public Fish fish;

    public FishItem(int minWeight, int maxWeight) {
        super(new Item.Properties().group(Aquaculture.TAB));
        this.fish = new Fish(minWeight, maxWeight);
    }

    /*@Override
    @Nonnull
    public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
        if (stack.hasTag() && stack.getTag() != null) {
            if (stack.getTag().contains("Prefix")) {
                return new TranslationTextComponent(stack.getTag().getString("Prefix") + " " + super.getDisplayName(stack));
            }
        }
        return super.getDisplayName(stack);
    }*/

    /*@Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<ITextComponent> toolTip, ITooltipFlag tooltipType) { //TODO Move to event. Config. Add support for non Aquaculture fish
        if (stack.hasTag() && stack.getTag() != null) {
            if (stack.getTag().contains("Weight")) {
                float weight = stack.getTag().getFloat("Weight");

                DecimalFormat df = new DecimalFormat("#,###.##");
                BigDecimal bd = new BigDecimal(weight);
                bd = bd.round(new MathContext(3));
                if (bd.doubleValue() > 999) {
                    toolTip.add(new TranslationTextComponent("Weight: " + df.format((int) bd.doubleValue()) + "lb"));
                } else {
                    toolTip.add(new TranslationTextComponent("Weight: " + bd + "lb"));
                }
            }
        }
    }*/

    public void assignRandomWeight(@Nonnull ItemStack stack, int heavyLineLvl) {
        if (stack.isEmpty()) {
            return;
        }

        Fish f = null;

        if (f.maxWeight == 1 && f.minWeight == 1)
            return;

        float min = f.minWeight;

        min += (f.maxWeight - min) * (0.1 * heavyLineLvl);

        float weight = new Random().nextFloat() * (f.maxWeight - min) + min;

        if (!stack.hasTag()) {
            stack.setTag(new CompoundNBT());
        }

        Preconditions.checkNotNull(stack.getTag(), "tagCompound");
        stack.getTag().putFloat("Weight", weight);

        if (weight <= f.maxWeight * 0.10F) {
            stack.getTag().putString("Prefix", "Juvenile");
        } else if (weight > f.maxWeight * 0.10F && weight <= f.maxWeight * 0.20F) {
            stack.getTag().putString("Prefix", "Small");
        } else if (weight >= f.maxWeight * 0.80F && weight < f.maxWeight * 0.90F) {
            stack.getTag().putString("Prefix", "Large");
        } else if (weight >= f.maxWeight * 0.90F) {
            stack.getTag().putString("Prefix", "Massive");
        }
    }

    public class Fish {
        //public int filletAmount; //TODO?
        public int minWeight;
        public int maxWeight;

        public Fish(int minWeight, int maxWeight) {
            //this.filletAmount = filletAmount;
            this.minWeight = minWeight;
            this.maxWeight = maxWeight;
        }
    }
}