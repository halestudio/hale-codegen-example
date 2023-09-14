import de.adv_online.www.namespaces.adv.gid._7_1.*;
import de.adv_online.www.namespaces.adv.gid._7_1.aa_modellarttype.Choice_1;
import eu.esdihumboldt.hale.common.core.io.report.IOReport;
import eu.esdihumboldt.hale.common.core.io.supplier.DefaultInputSupplier;
import eu.esdihumboldt.hale.common.core.io.supplier.FileIOSupplier;
import eu.esdihumboldt.hale.common.instance.geometry.DefaultGeometryProperty;
import eu.esdihumboldt.hale.common.instance.geometry.impl.CodeDefinition;
import eu.esdihumboldt.hale.common.instance.model.Instance;
import eu.esdihumboldt.hale.common.instance.model.InstanceCollection;
import eu.esdihumboldt.hale.common.instance.model.ResourceIterator;
import eu.esdihumboldt.hale.common.schema.geometry.CRSDefinition;
import eu.esdihumboldt.hale.common.schema.geometry.GeometryProperty;
import eu.esdihumboldt.hale.common.schema.model.impl.DefaultSchemaSpace;
import eu.esdihumboldt.hale.io.gml.reader.internal.GmlInstanceReader;
import eu.esdihumboldt.hale.io.gml.writer.GmlInstanceWriter;
import eu.esdihumboldt.hale.io.xsd.reader.XmlSchemaReader;
import net.opengis.www.gml._3_2.*;
import net.opengis.www.gml._3_2.abstractgeometry.Choice;
import org.eclipse.equinox.nonosgi.registry.RegistryFactoryHelper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.*;
import to.wetransform.hale.codegen.instances.InstanceConverter;
import to.wetransform.hale.codegen.model.ModelObject;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import static org.junit.Assert.*;

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

    GeometryPropertyType position = new GeometryPropertyType();
    Choice geom = new Choice();
    SurfaceType gmlSurface = new SurfaceType();
    CRSDefinition crs = new CodeDefinition("EPSG:4326");
    GeometryFactory factory = new GeometryFactory();
    LinearRing outer = factory
        .createLinearRing(
            new Coordinate[] { new Coordinate(49.87367, 8.65714),
                new Coordinate(49.87362, 8.65741), new Coordinate(49.87365,
                    8.65758),
                new Coordinate(49.87396, 8.65748),
                new Coordinate(49.87399, 8.65766),
                new Coordinate(49.87362, 8.65778),
                new Coordinate(49.87365, 8.65796), new Coordinate(49.874, 8.65785),
                new Coordinate(49.87409, 8.65856),
                new Coordinate(49.87403, 8.65858), new Coordinate(49.87402, 8.6585),
                new Coordinate(49.87365, 8.65863),
                new Coordinate(49.87362, 8.65874),
                new Coordinate(49.87369, 8.65873), new Coordinate(49.87372,
                    8.65907),
                new Coordinate(49.87343, 8.65915),
                new Coordinate(49.87339, 8.65884),
                new Coordinate(49.87356, 8.65874),
                new Coordinate(49.87353, 8.65864),
                new Coordinate(49.87348, 8.65867),
                new Coordinate(49.87339, 8.65805), new Coordinate(49.8736, 8.65797),
                new Coordinate(49.87358, 8.6578),
                new Coordinate(49.87336, 8.65788), new Coordinate(49.87332,
                    8.65768),
                new Coordinate(49.87356, 8.65758),
                new Coordinate(49.87354, 8.65745),
                new Coordinate(49.87347, 8.65744),
                new Coordinate(49.87338, 8.65723), new Coordinate(49.8734, 8.65701),
                new Coordinate(49.8736, 8.65698),
                new Coordinate(49.87367, 8.65714) });
    Polygon poly = factory.createPolygon(outer);
    gmlSurface.setGeometry(new DefaultGeometryProperty<>(crs, poly));
    geom.setGmlSurface(gmlSurface );
    position.setAbstractGeometry(geom );
    flurstueck.setPosition(position);

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
    reader.setSource(new DefaultInputSupplier(URI.create("https://repository.gdi-de.org/schemas/adv/nas/7.1/aaa.xsd")));
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

    // test that geometry is there and is the same as the polygon that was written
    assertNotNull(fs2.getPosition());
    SurfaceType check_surface = fs2.getPosition().getAbstractGeometry().getGmlSurface();
    GeometryProperty<?> prop = check_surface.getGeometry();
    assertNotNull(prop);
    Geometry check_geom = prop.getGeometry();
    assertTrue(poly.equals(check_geom));

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
    reader.setSource(new DefaultInputSupplier(URI.create("https://repository.gdi-de.org/schemas/adv/nas/7.1/aaa.xsd")));
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
    gmlReader.setSource(new DefaultInputSupplier(getClass().getClassLoader().getResource("getfeature-response_aaa71.pretty.xml").toURI()));

    IOReport readReport = gmlReader.execute(null);
    assertTrue(readReport.isSuccess());

    InstanceCollection readInstances = gmlReader.getInstances();

    // convert to model objects
    InstanceConverter converter = new InstanceConverter();
    Iterable<? extends ModelObject> readObjects = converter.convert(readInstances, new Model());
    Iterator<? extends ModelObject> it = readObjects.iterator();
    int count = 0;
    int countBuilding = 0;
    int countParcel = 0;
    while (it.hasNext()) {
      Object readObject = it.next();
      assertTrue(readObject instanceof ModelObject);
      count++;

      if (readObject instanceof AX_FlurstueckType) {
        countParcel++;
      }
      if (readObject instanceof AX_GebaeudeType) {
        countBuilding++;
      }
    }

    assertEquals(18, count);
    assertEquals(1, countParcel);
    assertEquals(1, countBuilding);
  }

}
