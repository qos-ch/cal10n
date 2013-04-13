package ch.qos.cal10n.verifier.processor;

import ch.qos.cal10n.util.AbstractCAL10NBundleFinder;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.net.URL;

/**
 * @since 0.8.1
 */
public class CAL10NBundleFinderByProcessingFiler extends AbstractCAL10NBundleFinder {

  final Filer filer;

  public CAL10NBundleFinderByProcessingFiler(Filer filer) {
    this.filer = filer;
  }

  @Override
  public URL getResource(String resourceCandidate) {
    try {
      FileObject fo = filer.getResource(StandardLocation.CLASS_OUTPUT, "", resourceCandidate);
      if (fo == null)
        return null;
      else
        return fo.toUri().toURL();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

  }
}
