var Opcodes = org.objectweb.asm.Opcodes;

function initializeCoreMod() {
    return {
        'no-bad-omen-from-pillager': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.item.FireChargeItem',
                'methodName': 'func_195939_a', //#onItemUse
                'methodDesc': '(Lnet/minecraft/item/ItemUseContext;)Lnet/minecraft/util/ActionResultType;'
            },
            'transformer': function(method) {
                print("[SavageAndRavage/COREMOD] Patching onItemUse() in FireChargeItem");
                for (var i = 0; i < method.instructions.size(); ++i) {
                	var instruction = method.instructions.get(i);
                }
                return method;
            }
        }
    }
}