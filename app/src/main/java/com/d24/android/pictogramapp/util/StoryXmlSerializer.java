package com.d24.android.pictogramapp.util;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;

import com.d24.android.pictogramapp.R;
import com.d24.android.pictogramapp.stickerview.StickerImageView;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Utility class for serializing a story as an XML file.
 */
public class StoryXmlSerializer {

	/**
	 * Writes an XML file containing information about the active story. This method collects
	 * information from the given ViewPager, it's children fragments and their children
	 * StickerViews, in order to make it possible to regenerate the story from the resulting XML
	 * file. Each fragment is stored as a {@code <scene>} node, and each StickerView is stored
	 * as a child {@code <figure>} node. An example of the returned XML:
	 *
	 * <pre>{@code
	 * <?xml version='1.0' encoding='utf-8' ?>
	 * <story name="My very own story">
	 *   <scene background="#fffff9c4">
	 *     <figure id="29" position="100.61575 356.74945" size="300 300" rotation="0.0" mirrored="false" />
	 *     <figure id="1" position="330.33588 363.47583" size="300 300" rotation="0.0" mirrored="false" />
	 *   </scene>
	 *   <scene>
	 *       ...
	 *   </scene>
	 * </story>
	 * }</pre>
	 *
	 * @param out the stream of which the XML will be written to
	 * @param pager the scene view, containing all scenes (fragments)
	 * @param name the name of the story
	 * @throws IOException if the stream is unable to output the XML, or if the given story is not serializable
	 */
	public void write(OutputStream out, ViewPager pager, String name) throws IOException {
		try {
			XmlSerializer serializer = Xml.newSerializer();
			serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
			serializer.setOutput(out, null);
			writeStory(serializer, pager, name);
		} finally {
			out.close();
		}
	}

	private void writeStory(XmlSerializer serializer, ViewPager pager, String name) throws IOException {
		serializer.startDocument("utf-8", null);

		serializer.startTag(null, "story");
		serializer.attribute(null, "name", name);
		// serializer.attribute(null, "author", author);

		for (int i = 0; i < pager.getChildCount(); ++i) {
			writeScene(serializer, pager.getChildAt(i));
		}

		serializer.endTag(null, "story");
		serializer.endDocument();
	}

	private void writeScene(XmlSerializer serializer, View scene) throws IOException {
		serializer.startTag(null, "scene");

		Drawable background = scene.getBackground();
		if (background instanceof ColorDrawable) {
			int colorValue = ((ColorDrawable) background).getColor();
			String hexFormat = "#" + Integer.toHexString(colorValue);
			serializer.attribute(null, "background", hexFormat);

			// To convert back to integer: Color.parseColor(hexFormat)
		} else {
			// TODO Determine drawable resource
		}

		if (scene instanceof ViewGroup) {
			ViewGroup group = (ViewGroup) scene;

			for (int i = 0; i < group.getChildCount(); ++i) {
				StickerImageView view = (StickerImageView) group.getChildAt(i);
				serializer.startTag(null, "figure");
				serializer.attribute(null, "id", String.valueOf((int) view.getTag(R.integer.tag_resource)));
				serializer.attribute(null, "position", view.getX() + " " + view.getY());
				serializer.attribute(null, "size", view.getWidth() + " " + view.getHeight());
				serializer.attribute(null, "rotation", String.valueOf(view.getRotation()));
				serializer.attribute(null, "mirrored", String.valueOf(view.isFlip()));
				serializer.endTag(null, "figure");
			}
		}

		serializer.endTag(null, "scene");
	}
}
