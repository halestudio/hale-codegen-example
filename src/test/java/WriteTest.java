import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

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
import eu.esdihumboldt.hale.common.schema.model.SchemaSpace;
import eu.esdihumboldt.hale.common.schema.model.impl.DefaultSchemaSpace;
import eu.esdihumboldt.hale.io.gml.writer.GmlInstanceWriter;
import eu.esdihumboldt.hale.io.xsd.reader.XmlSchemaReader;
import net.opengis.www.gml._3_2.AreaType;
import net.opengis.www.gml._3_2.CodeType;
import net.opengis.www.gml._3_2.CodeWithAuthorityType;
import to.wetransform.hale.codegen.instances.InstanceConverter;
import to.wetransform.hale.codegen.model.ModelObject;

public class WriteTest {

  @BeforeClass
  public static void init() {
    RegistryFactoryHelper.getRegistry();
  }

  @Test
  public void testSimpleWrite() throws Exception {
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

    //TODO validate when a valid instance is built

    System.out.println(tmpFile.toAbsolutePath().toString());
  }

}
