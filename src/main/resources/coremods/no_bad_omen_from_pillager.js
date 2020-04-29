var Opcodes = org.objectweb.asm.Opcodes;

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
                print("[SavageAndRavage/COREMOD] Patching onDeath() in AbstractRaiderEntity");
                for (var i = 0; i < method.instructions.size(); ++i) {
                	var instruction = method.instructions.get(i);
                    if(instruction.getOpcode()==Opcodes.INVOKEVIRTUAL && instruction.getNext().getOpcode()==Opcodes.POP){
                            var instruction2 = instruction.getPrevious();
                            var instruction1 = instruction2.getPrevious();
                            var instruction3 = instruction.getNext();
                            method.instructions.remove(instruction1);
                            print("[SavageAndRavage/COREMOD] Removed instruction "+instruction1);
                            method.instructions.remove(instruction2);
                            print("[SavageAndRavage/COREMOD] Removed instruction "+instruction2);
                            method.instructions.remove(instruction);
                            print("[SavageAndRavage/COREMOD] Removed instruction "+instruction);
                            method.instructions.remove(instruction3);
                            print("[SavageAndRavage/COREMOD] Removed instruction "+instruction3);
                            //if(instruction.getPrevious().getOpcode()==Opcodes.GETSTATIC){
                            //else if((instruction.getPrevious().getOpcode()==Opcodes.ALOAD)){
                    }
                }
                return method;
            }
        }
    }
}