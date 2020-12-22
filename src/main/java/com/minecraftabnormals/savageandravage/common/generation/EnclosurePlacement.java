package com.minecraftabnormals.savageandravage.common.generation;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;
import java.util.stream.Stream;

public class EnclosurePlacement extends Placement<NoPlacementConfig> {

    public EnclosurePlacement(Codec<NoPlacementConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public Stream<BlockPos> getPositions(WorldDecoratingHelper helper, Random rand, NoPlacementConfig config, BlockPos pos) {
        //this doesn't do the nearest thing, it requires you to loop through block posses manually - is there an equivalent function?
        /*BlockPos nearestOutpostPos = Structure.PILLAGER_OUTPOST.func_236388_a_(helper.field_242889_a.getWorld(),);
        if(nearestOutpostPos != null && positionsWithinRange(nearestOutpostPos, pos, 20, 50)) {
            //is there going to be a reliable way to check for other outposts or do i just have to do a probability thing?
            //maybe the feature itself can store a position and there's a way to get that?
            return Stream.of(pos);
            this dot already generated equals true??
        }*/
        return Stream.empty();
    }

    //TODO need a way to check for if another feature has generated without infinite recursion - maybe a custom method?

    private boolean positionsWithinRange(BlockPos centre, BlockPos external, double minimum, double maximum) {
        return centre.withinDistance(external, maximum) && !centre.withinDistance(external, minimum);
    }
}
