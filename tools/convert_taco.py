from pathlib import Path

import pandas as pd


PROJECT_ROOT = Path(__file__).resolve().parent.parent

INPUT_FILE = PROJECT_ROOT / "data" / "raw" / "Taco-4a-Edicao.xlsx"
OUTPUT_FILE = PROJECT_ROOT / "data" / "processed" / "taco_foods.csv"

SHEET_NAME = "CMVCol taco3"


COLUMN_NAMES = [
    "id",
    "nome",
    "umidade_percentual",
    "energia_kcal",
    "energia_kj",
    "proteina_g",
    "lipideos_g",
    "colesterol_mg",
    "carboidrato_g",
    "fibra_g",
    "cinzas_g",
    "calcio_mg",
    "magnesio_mg",
    "id_repetido",
    "manganes_mg",
    "fosforo_mg",
    "ferro_mg",
    "sodio_mg",
    "potassio_mg",
    "cobre_mg",
    "zinco_mg",
    "retinol_mcg",
    "re_mcg",
    "rae_mcg",
    "tiamina_mg",
    "riboflavina_mg",
    "piridoxina_mg",
    "niacina_mg",
    "vitamina_c_mg",
]


def normalize_value(value):
    """
    Converte os valores especiais da TACO.

    Número -> mantém número
    'Tr'   -> 0.0
    'NA'   -> None
    vazio  -> None
    """
    if pd.isna(value):
        return None

    if isinstance(value, str):
        value = value.strip()

        if value == "":
            return None

        if value.lower() == "tr":
            return 0.0

        if value.lower() == "na":
            return None

    return value


def load_taco():
    return pd.read_excel(
        INPUT_FILE,
        sheet_name=SHEET_NAME,
        header=None,
        skiprows=3,
        names=COLUMN_NAMES,
    )

def normalize_non_negative(value, tolerance=0.1):
    value = normalize_value(value)

    if value is None:
        return None

    if isinstance(value, (int, float)):

        if -tolerance <= value < 0:
            return 0.0

    return value

def process_taco(df):
    foods = []

    current_category = None

    IGNORED_TEXT_ROWS = {
        "Número do",
        "Alimento",
        "Legenda",
        "*",
        "†",
        "††",
        "†††",
        "††††",
    }

    for _, row in df.iterrows():

        food_id = row["id"]

        # Linhas como "Cereais e derivados"
        if isinstance(food_id, str):
            text = food_id.strip()

            if text in IGNORED_TEXT_ROWS:
                continue

            if text:
                current_category = text

            continue

        # Ignora linhas sem número de alimento
        if pd.isna(food_id):
            continue

        food = {
            "id": int(food_id),
            "nome": row["nome"],
            "categoria": current_category,

            "umidade_percentual": normalize_value(
                row["umidade_percentual"]
            ),

            "energia_kcal": normalize_value(
                row["energia_kcal"]
            ),

            "energia_kj": normalize_value(
                row["energia_kj"]
            ),

            "proteina_g": normalize_value(
                row["proteina_g"]
            ),

            "lipideos_g": normalize_value(
                row["lipideos_g"]
            ),

            "colesterol_mg": normalize_value(
                row["colesterol_mg"]
            ),

            "carboidrato_g": normalize_non_negative(
                row["carboidrato_g"]
            ),

            "fibra_g": normalize_value(
                row["fibra_g"]
            ),

            "cinzas_g": normalize_value(
                row["cinzas_g"]
            ),

            "calcio_mg": normalize_value(
                row["calcio_mg"]
            ),

            "magnesio_mg": normalize_value(
                row["magnesio_mg"]
            ),

            "manganes_mg": normalize_value(
                row["manganes_mg"]
            ),

            "fosforo_mg": normalize_value(
                row["fosforo_mg"]
            ),

            "ferro_mg": normalize_value(
                row["ferro_mg"]
            ),

            "sodio_mg": normalize_value(
                row["sodio_mg"]
            ),

            "potassio_mg": normalize_value(
                row["potassio_mg"]
            ),

            "cobre_mg": normalize_value(
                row["cobre_mg"]
            ),

            "zinco_mg": normalize_value(
                row["zinco_mg"]
            ),

            "retinol_mcg": normalize_value(
                row["retinol_mcg"]
            ),

            "re_mcg": normalize_value(
                row["re_mcg"]
            ),

            "rae_mcg": normalize_value(
                row["rae_mcg"]
            ),

            "tiamina_mg": normalize_value(
                row["tiamina_mg"]
            ),

            "riboflavina_mg": normalize_value(
                row["riboflavina_mg"]
            ),

            "piridoxina_mg": normalize_value(
                row["piridoxina_mg"]
            ),

            "niacina_mg": normalize_value(
                row["niacina_mg"]
            ),

            "vitamina_c_mg": normalize_value(
                row["vitamina_c_mg"]
            ),
        }

        foods.append(food)

    return pd.DataFrame(foods)


def validate_taco(df):
    print("\n=== VALIDAÇÃO ===")

    errors = []
    warnings = []

    # 1. Quantidade esperada de alimentos
    expected_food_count = 597

    if len(df) != expected_food_count:
        errors.append(
            f"Quantidade de alimentos incorreta: "
            f"esperado={expected_food_count}, encontrado={len(df)}"
        )
    else:
        print(f"[OK] {len(df)} alimentos encontrados.")

    # 2. IDs duplicados
    duplicated_ids = df[df["id"].duplicated(keep=False)]

    if not duplicated_ids.empty:
        errors.append(
            f"Existem IDs duplicados: "
            f"{duplicated_ids['id'].tolist()}"
        )
    else:
        print("[OK] Nenhum ID duplicado.")

    # 3. Verificar intervalo dos IDs
    expected_ids = set(range(1, expected_food_count + 1))
    actual_ids = set(df["id"])

    missing_ids = expected_ids - actual_ids

    if missing_ids:
        errors.append(
            f"IDs ausentes: {sorted(missing_ids)}"
        )
    else:
        print("[OK] IDs de 1 a 597 presentes.")

    # 4. Nome vazio
    missing_names = df["nome"].isna().sum()

    if missing_names > 0:
        errors.append(
            f"{missing_names} alimento(s) sem nome."
        )
    else:
        print("[OK] Todos os alimentos possuem nome.")

    # 5. Categoria vazia
    missing_categories = df["categoria"].isna().sum()

    if missing_categories > 0:
        errors.append(
            f"{missing_categories} alimento(s) sem categoria."
        )
    else:
        print("[OK] Todos os alimentos possuem categoria.")

    # 6. Calorias negativas
    negative_calories = df[
        pd.to_numeric(
            df["energia_kcal"],
            errors="coerce"
        ) < 0
    ]

    if not negative_calories.empty:
        errors.append(
            "Foram encontradas calorias negativas."
        )
    else:
        print("[OK] Nenhuma energia negativa.")

    # 7. Macronutrientes negativos
    nutrient_columns = [
        "proteina_g",
        "lipideos_g",
        "carboidrato_g",
        "fibra_g",
    ]

    for column in nutrient_columns:

        numeric_values = pd.to_numeric(
            df[column],
            errors="coerce"
        )

        invalid_mask = numeric_values < 0

        if invalid_mask.any():
            invalid_rows = df.loc[
                invalid_mask,
                ["id", "nome", column]
            ]

            errors.append(
                f"Valores negativos encontrados em {column}:\n"
                f"{invalid_rows.to_string(index=False)}"
            )
    if not any(
        "negativos encontrados" in error
        for error in errors
    ):
        print("[OK] Nenhum macronutriente negativo.")

    # 8. Valores acima de 100 g / 100 g
    for column in nutrient_columns:

        numeric_values = pd.to_numeric(
            df[column],
            errors="coerce"
        )

        invalid = numeric_values > 100

        if invalid.any():
            warnings.append(
                f"{column}: "
                f"{invalid.sum()} valor(es) acima de 100 g/100 g."
            )

    # Resultado
    print()

    if warnings:
        print("Avisos:")

        for warning in warnings:
            print(f"[AVISO] {warning}")

    if errors:
        print("\nErros:")

        for error in errors:
            print(f"[ERRO] {error}")

        raise ValueError(
            "A validação da TACO falhou."
        )


    categories = sorted(
        df["categoria"]
        .dropna()
        .unique()
    )

    print(
        f"[OK] {len(categories)} categorias encontradas:"
    )

    for category in categories:
        print(f"     - {category}")

    duplicated_names = df[
        df["nome"].duplicated(keep=False)
    ]

    if not duplicated_names.empty:
        warnings.append(
            f"{len(duplicated_names)} registros possuem "
            "descrições repetidas."
        )

    print("\n=== RESUMO ===")

    print(
        df[
            [
                "energia_kcal",
                "proteina_g",
                "lipideos_g",
                "carboidrato_g",
                "fibra_g",
                "colesterol_mg",
            ]
        ].describe()
    )

    print("\nValidação concluída com sucesso.")




def main():
    print(f"Lendo: {INPUT_FILE}")

    df_raw = load_taco()

    print(f"Linhas brutas: {len(df_raw)}")

    df_foods = process_taco(df_raw)

    print(f"Alimentos encontrados: {len(df_foods)}")

    validate_taco(df_foods)

    OUTPUT_FILE.parent.mkdir(
        parents=True,
        exist_ok=True
    )

    df_foods.to_csv(
        OUTPUT_FILE,
        index=False,
        encoding="utf-8"
    )

    print(f"Arquivo criado: {OUTPUT_FILE}")

    print("\nPrimeiros alimentos:")
    print(
        df_foods[
            [
                "id",
                "nome",
                "categoria",
                "energia_kcal",
                "proteina_g",
                "carboidrato_g",
                "fibra_g",
                "colesterol_mg",
            ]
        ].head(10)
    )

    sample_names = [
        "Arroz, tipo 1, cozido",
        "Banana, prata, crua",
        "Frango, peito, sem pele, grelhado",
        "Ovo, de galinha, inteiro, cozido/10minutos",
        "Leite, de vaca, integral, UHT",
    ]

    samples = df_foods[
        df_foods["nome"].isin(sample_names)
    ]

    print("\n=== AMOSTRAS ===")
    print(
        samples[
            [
                "id",
                "nome",
                "categoria",
                "energia_kcal",
                "proteina_g",
                "lipideos_g",
                "carboidrato_g",
                "fibra_g",
                "colesterol_mg",
            ]
        ].to_string(index=False)
    )

if __name__ == "__main__":
    main()