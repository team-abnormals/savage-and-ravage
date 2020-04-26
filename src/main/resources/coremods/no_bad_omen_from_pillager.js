var Opcodes = org.objectweb.asm.Opcodes;
var VarInsnNode = org.objectweb.asm.tree.VarInsnNode;
var InsnList = org.objectweb.asm.tree.InsnList;

function initializeCoreMod() {
    return {
        'no-bad-omen-from-pillager': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.monster.AbstractRaiderEntity',
                'methodName': 'func_70645_a', //#onDeath
                'methodDesc': '(Lnet/minecraft/util/DamageSource;)V'
            },
            'transformer': function(method) {
                var instructionNumber = method.instructions.size();
                for (var i = 0; i < instructionNumber; ++i) {
                	var instruction = method.instructions.get(i);
                	print("The current opcode is"+instruction.getOpcode())
                }

                return method;
            }
        }
    }
}