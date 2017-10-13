/**
 * Typed AST of an effect; parses the definition of the effect 
 * from a String into a set of effects.
 * 
 * @author vzhao
 */

package wyvern.tools.typedAST.core.declarations;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import wyvern.target.corewyvernIL.decltype.DeclType;
import wyvern.target.corewyvernIL.decltype.EffectDeclType;
import wyvern.target.corewyvernIL.effects.Effect;
import wyvern.target.corewyvernIL.effects.EffectSet;
import wyvern.target.corewyvernIL.modules.TypedModuleSpec;
import wyvern.target.corewyvernIL.support.GenContext;
import wyvern.target.corewyvernIL.support.TopLevelContext;
import wyvern.tools.errors.FileLocation;
import wyvern.tools.typedAST.abs.Declaration;
import wyvern.tools.typedAST.interfaces.TypedAST;
import wyvern.tools.types.Environment;
import wyvern.tools.types.Type;
import wyvern.tools.util.EvaluationEnvironment;

public class EffectDeclaration extends Declaration {
	private String name;
	private EffectSet effectSet;
	private FileLocation loc;
	
	public EffectDeclaration(String name, String effects, FileLocation fileLocation) {	
		this.name = name;
		this.loc = fileLocation;
		
		/* Parses the String effects into a set. If it was not defined: 
		 * effectSet==null if in type signature, else error is reported */
		this.effectSet = EffectSet.parseEffects(name, effects, true, fileLocation);
	}
	
	public Effect getEffect() {
		return new Effect(null, getName(), getLocation()); 
	}
	
	public void doPrettyPrint(Appendable dest, String indent) throws IOException {
		dest.append(indent).append("effect ").append(getName()).append(" = ");
		if (effectSet != null) {dest.append(effectSet.toString());}
		dest.append('\n');
	}
	
	@Override
	public FileLocation getLocation() {
		return loc;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public EffectSet getEffectSet() {
		return effectSet;
	}
	
	@Override
	public DeclType genILType(GenContext ctx) {
		// for checking that the effects in effectSet are in scope (such as previously declared in the same type signature)
		if (effectSet != null) {	effectSet.verifyInType(ctx, getName());	}
		return new EffectDeclType(getName(), getEffectSet(), getLocation());
	}
	
	@Override
	public wyvern.target.corewyvernIL.decl.Declaration generateDecl(GenContext ctx, GenContext thisContext) {
		if (effectSet != null) { effectSet.addPaths(ctx); }
		return new wyvern.target.corewyvernIL.decl.EffectDeclaration(getName(), getEffectSet(), getLocation());
	}
	
	@Override
	public wyvern.target.corewyvernIL.decl.Declaration topLevelGen(GenContext ctx, List<TypedModuleSpec> dependencies) {
		return generateDecl(ctx, ctx); // like in DefDeclaration
	}
	
	@Override
	public void addModuleDecl(TopLevelContext tlc) {
		wyvern.target.corewyvernIL.decl.Declaration decl = topLevelGen(tlc.getContext(), null);
		DeclType dt = genILType(tlc.getContext());
		tlc.addModuleDecl(decl,dt);
	}
	
	
	/**** Secondary or obsolete (due to use of Environment) methods. ***/
	@Override
	public Environment extendType(Environment env, Environment against) {
		// TODO Auto-generated method stub
		throw new RuntimeException("extendType not implemented");
	}
	@Override
	public Environment extendName(Environment env, Environment against) {
		// TODO Auto-generated method stub
		throw new RuntimeException("extendName not implemented");
	}
	@Override
	public Type getType() { // effects have no parsed "type" like variables/values do
		throw new RuntimeException("extendName not implemented");
	}
	@Override
	public Map<String, TypedAST> getChildren() {
		// TODO Auto-generated method stub
		throw new RuntimeException("getChildren not implemented");
	}
	@Override
	public TypedAST cloneWithChildren(Map<String, TypedAST> newChildren) {
		// TODO Auto-generated method stub
		throw new RuntimeException("getChildren not implemented");
	}

	@Override
	protected Type doTypecheck(Environment env) { 
		throw new RuntimeException("doTypecheck not implemented");
	}
	@Override
	protected Environment doExtend(Environment old, Environment against) {
		// TODO Auto-generated method stub
		throw new RuntimeException("doExtend not implemented");
	}
	@Override
	public EvaluationEnvironment extendWithValue(EvaluationEnvironment old) {
		// TODO Auto-generated method stub
		throw new RuntimeException("extendWithValue not implemented");
	}
	@Override
	public void evalDecl(EvaluationEnvironment evalEnv, EvaluationEnvironment declEnv) {
		// TODO Auto-generated method stub
		throw new RuntimeException("evalDecl not implemented");
	}
	
}