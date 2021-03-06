/*
 * This file is part of Dynamic Surroundings, licensed under the MIT License (MIT).
 *
 * Copyright (c) OreCruncher
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.blockartistry.DynSurround.client.swing;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

@SuppressWarnings("serial")
@SideOnly(Side.CLIENT)
public class BlockViewer extends JPanel implements Observer {

	private final JLabel lblNewLabel = new JLabel("Block Name:");
	private final JLabel lblBlockMaterial = new JLabel("Block Material:");
	private final JLabel lblNewLabel_1 = new JLabel("Acoustics:");
	private final JScrollPane scrollPane = new JScrollPane();
	private final JScrollPane scrollPane_1 = new JScrollPane();
	private final JScrollPane scrollPane_2 = new JScrollPane();
	private final JLabel lblBlockEffects = new JLabel("Block Effects:");
	private final JLabel lblSounds = new JLabel("Sounds:");
	private final JLabel lblOreDictionary = new JLabel("Ore Dictionary:");

	private final JLabel blockName = new JLabel("block name");
	private final JLabel blockMaterial = new JLabel("material");
	private final JList<String> footstepAcoustics = new JList<String>();
	private final JList<String> blockEffects = new JList<String>();
	private final JList<String> blockSounds = new JList<String>();
	private final JList<String> blockOreEntries = new JList<String>();

	protected final DataProxy.ViewedBlockData data;

	public BlockViewer() {
		setLayout(null);

		this.setName("Block Data");

		lblNewLabel.setBounds(10, 11, 100, 14);
		add(lblNewLabel);

		blockName.setBounds(120, 11, 320, 14);
		add(blockName);

		lblBlockMaterial.setBounds(10, 36, 100, 14);
		add(lblBlockMaterial);

		blockMaterial.setBounds(120, 36, 320, 14);
		add(blockMaterial);

		lblNewLabel_1.setBounds(10, 153, 100, 14);
		add(lblNewLabel_1);

		scrollPane.setBounds(120, 151, 320, 77);
		add(scrollPane);

		footstepAcoustics.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(footstepAcoustics);

		lblBlockEffects.setBounds(10, 245, 100, 14);
		add(lblBlockEffects);

		scrollPane_1.setBounds(120, 243, 320, 77);
		add(scrollPane_1);
		blockEffects.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPane_1.setViewportView(blockEffects);

		this.data = new DataProxy.ViewedBlockData();
		this.data.addObserver(this);
		lblSounds.setBounds(10, 333, 100, 14);

		add(lblSounds);
		scrollPane_2.setBounds(120, 331, 320, 77);

		add(scrollPane_2);
		blockSounds.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPane_2.setViewportView(blockSounds);

		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(120, 61, 320, 79);
		add(scrollPane_3);

		scrollPane_3.setViewportView(blockOreEntries);

		lblOreDictionary.setBounds(10, 63, 100, 14);
		add(lblOreDictionary);
	}

	protected String[] asArray(@Nonnull final List<String> list) {
		final String[] result = new String[list.size()];
		return list.toArray(result);
	}

	@Override
	public void update(Observable o, Object arg) {

		this.blockName.setText(this.data.getBlockName());
		this.blockMaterial.setText(this.data.getBlockMaterial());

		this.blockOreEntries.setListData(asArray(this.data.getBlockOreDictionary()));
		this.footstepAcoustics.setListData(asArray(this.data.getFootstepAcoustics()));
		this.blockEffects.setListData(asArray(this.data.getBlockEffects()));
		this.blockSounds.setListData(asArray(this.data.getBlockSounds()));

	}
}
