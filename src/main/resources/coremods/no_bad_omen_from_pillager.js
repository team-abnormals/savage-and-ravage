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
                for (var i = 0; i < method.instructions.size(); ++i) {
                    var instruction = method.instructions.get(i);
                	if((i>=154&&i<=155)||(i>=191&&i<=192)){ //these are the specific instructions I want to remove
                        method.instructions.remove(instruction);
                        print("[SavageAndRavage/COREMOD] Successfully removed instruction "+instruction+" in AbstractRaiderEntity, whose instruction number in onDeath() was "+i);
                    }
                }
                return method;
            }
        }
    }
}