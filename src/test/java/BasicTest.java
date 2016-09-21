import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.equinox.nonosgi.registry.RegistryFactoryHelper;
import org.junit.BeforeClass;
import org.junit.Test;

import de.adv_online.www.namespaces.adv.gid._6_0.AA_LebenszeitintervallPropertyType;
import de.adv_online.www.namespaces.adv.gid._6_0.AA_LebenszeitintervallType;
import de.adv_online.www.namespaces.adv.gid._6_0.AA_ModellartPropertyType;
import de.adv_online.www.namespaces.adv.gid._6_0.AA_ModellartType;
import de.adv_online.www.namespaces.adv.gid._6_0.AX_FlurstueckType;
import de.adv_online.www.namespaces.adv.gid._6_0.AX_FlurstuecksnummerPropertyType;
import de.adv_online.www.namespaces.adv.gid._6_0.AX_FlurstuecksnummerType;
import de.adv_online.www.namespaces.adv.gid._6_0.AX_Gemarkung_SchluesselPropertyType;
import de.adv_online.www.namespaces.adv.gid._6_0.AX_Gemarkung_SchluesselType;
import de.adv_online.www.namespaces.adv.gid._6_0.aa_modellarttype.Choice_1;
import eu.esdihumboldt.hale.common.core.io.report.IOReport;
import eu.esdihumboldt.hale.common.core.io.supplier.DefaultInputSupplier;
import eu.esdihumboldt.hale.common.core.io.supplier.FileIOSupplier;
import eu.esdihumboldt.hale.common.instance.model.Instance;
import eu.esdihumboldt.hale.common.instance.model.InstanceCollection;
import eu.esdihumboldt.hale.common.instance.model.ResourceIterator;
import eu.esdihumboldt.hale.common.schema.model.impl.DefaultSchemaSpace;
import eu.esdihumboldt.hale.io.gml.reader.internal.GmlInstanceReader;
import eu.esdihumboldt.hale.io.gml.writer.GmlInstanceWriter;
import eu.esdihumboldt.hale.io.xsd.reader.XmlSchemaReader;
import net.opengis.www.gml._3_2.AreaType;
import net.opengis.www.gml._3_2.CodeType;
import net.opengis.www.gml._3_2.CodeWithAuthorityType;
import to.wetransform.hale.codegen.instances.InstanceConverter;
import to.wetransform.hale.codegen.model.ModelObject;

public class BasicTest {

  @BeforeClass
  public static void init() {
    RegistryFactoryHelper.getRegistry();
  }

  @Test
  public void testSimpleWriteRead() throws Exception {
    // create object
    AX_FlurstueckType flurstueck = new AX_FlurstueckType();

    flurstueck.setGmlId("DEBYvAAAC0DbvKdN");

    CodeWithAuthorityType gmlIdentifier = new CodeWithAuthorityType();
    gmlIdentifier.setCodeSpace(URI.create("http://www.adv-online.de/"));
    gmlIdentifier.setValue("urn:adv:oid:DEBYvAAAC0DbvKdN");
    flurstueck.setGmlIdentifier(gmlIdentifier);

    AA_LebenszeitintervallPropertyType lebenszeitintervall = new AA_LebenszeitintervallPropertyType();
    AA_LebenszeitintervallType li = new AA_LebenszeitintervallType();
    li.setBeginnt(Timestamp.from(Instant.now()));
    lebenszeitintervall.setAA_Lebenszeitintervall(li);
    flurstueck.setLebenszeitintervall(lebenszeitintervall);

    AA_ModellartPropertyType ma1 = new AA_ModellartPropertyType();
    AA_ModellartType ma1_ = new AA_ModellartType();
    Choice_1 ma1_c = new Choice_1();
    ma1_c.setAdvStandardModell("DLKM");
    ma1_.setChoice_1(ma1_c );
    ma1.setAA_Modellart(ma1_);
    flurstueck.getModellart().add(ma1);

    CodeType anlass = new CodeType();
    anlass.setValue("000000");
    flurstueck.getAnlass().add(anlass);

    //TODO position/geometry

    AX_Gemarkung_SchluesselPropertyType gemarkung = new AX_Gemarkung_SchluesselPropertyType();
    AX_Gemarkung_SchluesselType gemarkungSchluessel = new AX_Gemarkung_SchluesselType();
    gemarkungSchluessel.setGemarkungsnummer("9998");
    gemarkungSchluessel.setLand("09");
    gemarkung.setAX_Gemarkung_Schluessel(gemarkungSchluessel);
    flurstueck.setGemarkung(gemarkung);

    AX_FlurstuecksnummerPropertyType flurstuecksnummer = new AX_FlurstuecksnummerPropertyType();
    AX_FlurstuecksnummerType flurstuecksnummer_ = new AX_FlurstuecksnummerType();
    flurstuecksnummer_.setZaehler("1");
    flurstuecksnummer_.setNenner("0");
    flurstuecksnummer.setAX_Flurstuecksnummer(flurstuecksnummer_ );
    flurstueck.setFlurstuecksnummer(flurstuecksnummer);

    flurstueck.setFlurstueckskennzeichen("099998___00001______");

    AreaType amtlicheFlaeche = new AreaType();
    amtlicheFlaeche.setUom("m2");
    amtlicheFlaeche.setValue(5117.0);
    flurstueck.setAmtlicheFlaeche(amtlicheFlaeche);

    flurstueck.setAbweichenderRechtszustand(false);

    // ...

    // load XML Schema
    XmlSchemaReader reader = new XmlSchemaReader();
    reader.setSource(new DefaultInputSupplier(getClass().getClassLoader().getResource("NAS_6.0.1/schema/aaa.xsd").toURI()));
    reader.setOnlyElementsMappable(true);
    IOReport report = reader.execute(null);
    if (!report.isSuccess()) {
      throw new IllegalStateException("Loading XML schema failed");
    }

    // convert
    InstanceConverter converter = new InstanceConverter();

    Collection<ModelObject> objects = Collections.singleton(flurstueck);
    InstanceCollection converted = converter.convert(objects, reader.getSchema());

    int num = 0;
    try (ResourceIterator<Instance> it = converted.iterator()) {
      while (it.hasNext()) {
        it.next();
        num++;
      }
    }

    assertEquals(1, num);

    // create temporary file
    Path tmpFile = Files.createTempFile("instances", ".gml");

    // write instance collection
    GmlInstanceWriter writer = new GmlInstanceWriter();
    writer.setPrettyPrint(true);
    writer.setInstances(converted);
    writer.setTarget(new FileIOSupplier(tmpFile.toFile()));
    DefaultSchemaSpace ss = new DefaultSchemaSpace();
    ss.addSchema(reader.getSchema());
    writer.setTargetSchema(ss);

    IOReport writeReport = writer.execute(null);
    assertTrue(writeReport.isSuccess());

    //TODO validate when a valid instance is built?

    System.out.println(tmpFile.toAbsolutePath().toString());

    // read instance collection
    GmlInstanceReader gmlReader = new GmlInstanceReader();
    gmlReader.setSourceSchema(ss);
    gmlReader.setSource(new FileIOSupplier(tmpFile.toFile()));

    IOReport readReport = gmlReader.execute(null);
    assertTrue(readReport.isSuccess());

    InstanceCollection readInstances = gmlReader.getInstances();

    // convert to model objects
    Iterable<? extends ModelObject> readObjects = converter.convert(readInstances, new Model());
    Iterator<? extends ModelObject> it = readObjects.iterator();
    assertTrue(it.hasNext());
    Object readObject = it.next();

    assertTrue(readObject instanceof AX_FlurstueckType);
    AX_FlurstueckType fs2 = (AX_FlurstueckType) readObject;

    // assertions on read object
    assertEquals("099998___00001______", fs2.getFlurstueckskennzeichen());

    assertNotNull(fs2.getModellart());
    assertEquals(1, fs2.getModellart().size());
    AA_ModellartPropertyType check_modellart = fs2.getModellart().get(0);
    AA_ModellartType check_modellart_ = check_modellart.getAA_Modellart();
    Choice_1 check_modellart_choice = check_modellart_.getChoice_1();
    assertNull(check_modellart_choice.getSonstigesModell());
    String check_advmodell = check_modellart_choice.getAdvStandardModell();
    assertNotNull(check_advmodell);
    assertEquals("DLKM", check_advmodell);

    // ...

    // not any more objects expected
    assertFalse(it.hasNext());

    // delete temporarily created file
    Files.delete(tmpFile);
  }

  @Test
  public void testSampleRead() throws Exception {
    // load XML Schema
    XmlSchemaReader reader = new XmlSchemaReader();
    reader.setSource(new DefaultInputSupplier(getClass().getClassLoader().getResource("NAS_6.0.1/schema/aaa.xsd").toURI()));
    reader.setOnlyElementsMappable(true);
    IOReport report = reader.execute(null);
    if (!report.isSuccess()) {
      throw new IllegalStateException("Loading XML schema failed");
    }

    // read instance collection
    GmlInstanceReader gmlReader = new GmlInstanceReader();
    DefaultSchemaSpace ss = new DefaultSchemaSpace();
    ss.addSchema(reader.getSchema());
    gmlReader.setSourceSchema(ss);
    gmlReader.setSource(new DefaultInputSupplier(getClass().getClassLoader().getResource("getfeature-response.xml").toURI()));

    IOReport readReport = gmlReader.execute(null);
    assertTrue(readReport.isSuccess());

    InstanceCollection readInstances = gmlReader.getInstances();

    // convert to model objects
    InstanceConverter converter = new InstanceConverter();
    Iterable<? extends ModelObject> readObjects = converter.convert(readInstances, new Model());
    Iterator<? extends ModelObject> it = readObjects.iterator();
    int count = 0;
    while (it.hasNext()) {
      Object readObject = it.next();
      assertTrue(readObject instanceof ModelObject);
      count++;
    }

    assertEquals(463, count);
  }

}
