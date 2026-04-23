-- 1. Busca de Pets por Tutor
CREATE INDEX IF NOT EXISTS idx_pet_tutor_id 
ON pet (tutor_id);

-- 2. Busca de Consultas por Pet
CREATE INDEX IF NOT EXISTS idx_consulta_pet_id 
ON consulta (pet_id);

-- 3. Busca de Consultas por Veterinário (Cobrança simples)
CREATE INDEX IF NOT EXISTS idx_consulta_veterinario_id 
ON consulta (veterinario_id);

-- 4. Índice Funcional B-Tree (Otimiza: WHERE c.veterinario_id = ? AND DATE(c.data_consulta) = ?)
CREATE INDEX IF NOT EXISTS idx_consulta_vet_data_func 
ON consulta (veterinario_id, (DATE(data_consulta)));