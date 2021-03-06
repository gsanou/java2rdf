//package uk.ac.ebi.fg.java2rdf.mapping.properties;
//
//import static info.marcobrandizi.rdfutils.commonsrdf.CommonsRDFUtils.COMMUTILS;
//
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.Map;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.Validate;
//
//import uk.ac.ebi.fg.java2rdf.mapping.RdfMapperFactory;
//import uk.ac.ebi.fg.java2rdf.mapping.RdfMappingException;
//import uk.ac.ebi.fg.java2rdf.mapping.rdfgen.RdfUriGenerator;
//import uk.ac.ebi.fg.java2rdf.mapping.rdfgen.RdfValueGenerator;
//
///**
// * Maps a Java String property, which is assumed to contain an URI (or part of it), onto an RDF statement based on a 
// * property like rdfs:seeAlso or even an object property. 
// *
// * <dl><dt>date</dt><dd>23 Oct 2013</dd></dl>
// * @author Marco Brandizi
// *
// */
//public class UriStringPropRdfMapper<T> extends UriProvidedPropertyRdfMapper<T, String>
//{
//	public UriStringPropRdfMapper ()  {
//		super ();
//	}
//
//	public UriStringPropRdfMapper ( String targetPropertyUri ) {
//		super ( targetPropertyUri );
//	}
//	
//	
//	/**
//	 * <p>Generates the RDF triple 
//	 * ({@link #getMapperFactory() getMapperFactory(source, params)}, {@link #getTargetPropertyUri()}, 
//	 *   new {@link URI} ( propValue ) ).</p> 
//	 *   
//	 * <p>If propValue cannot be resolved to a proper URI, doesn't spawn anything and 
//	 * logs an error (but doesn't throw any exception).</p>
//	 * 
//	 */
//	@Override
//	public boolean map ( T source, String propValue, Map<String, Object> params )
//	{
//		propValue = StringUtils.trimToNull ( propValue );
//		if ( !super.map ( source, propValue, params ) ) return false;
//		
//		try
//		{
//			RdfMapperFactory mapFactory = this.getMapperFactory ();
//			Validate.notNull ( mapFactory, "Internal error: %s must be linked to a mapper factory", this.getClass ().getSimpleName () );
//			
//			// TODO: can we avoid to keep recomputing these
//			//
//			// This is necessary, cause source/pval may be swapped by InversePropRdfMapper
//			String subjUri = mapFactory.getUri ( source, params );
//			if ( subjUri == null ) return false;
//
//			RdfUriGenerator<String> uriGenerator = this.getRdfUriGenerator ();
//			String objUri = uriGenerator != null 
//				? uriGenerator.getUri ( propValue ) 
//				: new URI ( propValue ).toASCIIString ();
//			
//			COMMUTILS.assertResource ( this.getMapperFactory ().getGraphModel (), subjUri, this.getTargetPropertyUri (), objUri );
//			
//			return true;
//		} 
//		catch ( URISyntaxException ex )
//		{
//			log.error ( "Ignoring bad URI while doing RDF mapping <{}[{}] '{}' [{}]",
//				source.getClass ().getSimpleName (), 
//				StringUtils.abbreviate ( source.toString (), 50 ), 
//				this.getTargetPropertyUri (),
//				StringUtils.abbreviate ( propValue.toString (), 50 ) 
//			);
//			return false;
//		}
//		catch ( Exception ex )
//		{
//			throw new RdfMappingException ( String.format ( 
//				"Error while doing the RDF mapping <%s[%s] '%s' [%s]: %s", 
//					source.getClass ().getSimpleName (), 
//					StringUtils.abbreviate ( source.toString (), 50 ), 
//					this.getTargetPropertyUri (),
//					StringUtils.abbreviate ( propValue.toString (), 50 ), 
//					ex.getMessage ()
//			), ex );
//		}
//	}
//	
//	
//	/**
//	 * This generates the URI value out of the target value of the JavaBean property that this mapper deals with.
//	 * If null, simply turns the Java value onto a URI.
//	 * 
//	 * This is a convenience wrapper of {@link #getRdfValueGenerator()}
//	 */
//	public RdfUriGenerator<String> getRdfUriGenerator () {
//		return (RdfUriGenerator<String>) this.getRdfValueGenerator ();
//	}
//
//	public void setRdfUriGenerator ( RdfUriGenerator<String> rdfUriGenerator ) {
//		this.setRdfValueGenerator ( rdfUriGenerator );
//	}
//
//	
//	/** 
//	 * Forces the type to be a {@link RdfUriGenerator}.
//	 */
//	@Override
//	public void setRdfValueGenerator ( RdfValueGenerator<String> rdfValueGenerator )
//	{
//		if ( ! ( rdfValueGenerator != null && rdfValueGenerator instanceof RdfUriGenerator ) ) 
//			throw new IllegalArgumentException ( 
//				"setRdfValueGenerator() must get a type of type RdfUriGenerator for " + this.getClass ().getSimpleName () + 
//				", refusing the type " + rdfValueGenerator.getClass ().getName ()
//		); 
//		super.setRdfValueGenerator ( rdfValueGenerator );
//	}	
//}
