package com.volmit.iris.gen.v2;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import com.volmit.iris.gen.v2.scaffold.Hunk;
import com.volmit.iris.gen.v2.scaffold.layer.ProceduralStream;
import com.volmit.iris.object.NoiseStyle;

public class TestGen
{
	public static void gen(Player p)
	{
		p.teleport(new WorldCreator("t/" + UUID.randomUUID().toString()).generator(new ChunkGenerator()
		{
			//@builder
			ProceduralStream<BlockData> rock = NoiseStyle.STATIC.stream(1337)
					.select(new Material[] {
						Material.STONE, 
						Material.ANDESITE
					}).convertCached((m) -> m.createBlockData());
			ProceduralStream<Double> terrain = NoiseStyle.CELLULAR.stream(1337)
					.fit(1, 32)
					.zoom(1.75)
					.interpolate().bilinear(4)
					.into().starcast(4, 9);
					
			//@done

			@Override
			public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome)
			{
				ChunkData c = createChunkData(world);
				Hunk<BlockData> data = Hunk.view(c);
				terrain.fill2D(data, x * 16, z * 16, rock);

				return c;
			}
		}).createWorld().getSpawnLocation());
	}
}