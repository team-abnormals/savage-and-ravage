function initializeCoreMod() {
    return {
        'no-bad-omen-from-pillager': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.monster.AbstractRaiderEntity',
                'name': 'func_70645_a',
                'methodDesc': '(Lnet/minecraft/util/DamageSource;)V'
            },
            'transformer': function(method) {
                for(var i in method.instructions){
                    print("The current opcode is" + method.instructions.get(i).getOpcode());
                }
                return method;
            }
        }
    }
}