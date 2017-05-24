package com.d24.android.pictogramapp.util;

import android.util.Xml;

import com.d24.android.pictogramapp.model.Figure;
import com.d24.android.pictogramapp.model.Scene;
import com.d24.android.pictogramapp.model.Story;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Utility class for serializing a story as an XML file.
 */
public class StoryXmlSerializer {

	// No namespaces will be used in this occasion
	private static final String ns = null;

	/**
	 * Writes an XML file containing information about the active story. This method collects
	 * information from the provided Story, in a manner which should make it possible to regenerate
	 * the story from the resulting XML file. An example of the returned XML:
	 *
	 * <pre>{@code
	 * <?xml version='1.0' encoding='utf-8' ?>
	 * <story name="My very own story">
	 *   <scene background="#fffff9c4">
	 *     <figure id="29" x="100.61575", y="356.74945" size="300" rotation="0.0" mirrored="false" />
	 *     <figure id="33" x="330.33588", y="363.47583" size="300" rotation="0.0" mirrored="false" />
	 *     ...
	 *   </scene>
	 *   <scene>
	 *       ...
	 *   </scene>
	 * </story>
	 * }</pre>
	 *
	 * @param out the stream of which the XML will be written to
	 * @param story the story to be saved
	 * @throws IOException if the stream is unable to output the XML, or if the given story is not serializable
	 */
	public void write(OutputStream out, Story story) throws IOException {
		try {
			XmlSerializer serializer = Xml.newSerializer();
			serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
			serializer.setOutput(out, null);
			writeStory(serializer, story);
		} finally {
			out.close();
		}
	}

	private void writeStory(XmlSerializer serializer, Story story) throws IOException {
		serializer.startDocument("utf-8", null);

		serializer.startTag(ns, "story");
		if (story.title != null) serializer.attribute(ns, "title", story.title);

		for (Scene scene : story.scenes) {
			writeScene(serializer, scene);
		}

		serializer.endTag(ns, "story");
		serializer.endDocument();
	}

	private void writeScene(XmlSerializer serializer, Scene scene) throws IOException {
		serializer.startTag(ns, "scene");
		if (scene.background != null) serializer.attribute(ns, "background", scene.background);

		for (Figure figure : scene.figures) {
			serializer.startTag(ns, "figure");
			serializer.attribute(ns, "id", String.valueOf(figure.id));
			serializer.attribute(ns, "x", String.valueOf(figure.x));
			serializer.attribute(ns, "y", String.valueOf(figure.y));
			serializer.attribute(ns, "size", String.valueOf(figure.size));
			serializer.attribute(ns, "rotation", String.valueOf(figure.rotation));
			serializer.attribute(ns, "mirrored", String.valueOf(figure.mirrored));
			serializer.endTag(ns, "figure");
		}

		serializer.endTag(ns, "scene");
	}
}
