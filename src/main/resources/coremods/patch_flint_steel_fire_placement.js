var Opcodes = org.objectweb.asm.Opcodes;

function initializeCoreMod() {
    return {
        'patch-flint-steel-fire-placement': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.item.FlintAndSteelItem',
                'methodName': 'func_195939_a', //#onItemUse
                'methodDesc': '(Lnet/minecraft/item/ItemUseContext;)Lnet/minecraft/util/ActionResultType;'
            },
            'transformer': function(method) {
                print("[SavageAndRavage/COREMOD] Patching onItemUse in FlintAndSteelItem");
                var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                var newInstructions = new InsnList();

                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 4));
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1);
                newInstructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, 3));
                newInstructions.add(ASM.buildMethodCall(
                	"com/farcr/savageandravage/core/SRHooks",
                	"isBlockNotOminousBanner",
                	"(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/World;)Ljava/lang/Boolean",
                	ASM.MethodType.STATIC
                ));

                				method.instructions.insertBefore(method.instructions.getFirst(), methodInstructions);
                				return method;
                for (var i = 0; i < method.instructions.size(); ++i) {
                	var instruction = method.instructions.get(i);
                    if(instruction.getOpcode()==Opcodes.INVOKESTATIC && instruction.getPrevious().getPrevious().getOpcode()==Opcodes.INVOKEINTERFACE){

                    }
                }
                return method;
            }
        }
    }
}