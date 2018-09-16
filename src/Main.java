import org.apache.log4j.Logger;
import ru.valiullin.VendingMachine;
import ru.valiullin.drinks.DrinkType;
import ru.valiullin.exceptions.HumanFactorException;
import ru.valiullin.exceptions.OutOfStockException;

import java.util.Scanner;

public class Main {
    /**
     * Добавление лог сборщика
     * Почему то не добавилась строчка в .iml файл - <sourceFolder url="file://$MODULE_DIR$/resources" type="java-resource" />
     */
    private static final Logger LOG = Logger.getLogger(Main.class);

    private static VendingMachine vm = new VendingMachine();

    public static void main(String[] args) {
        LOG.info("Страрт работы программы.");
        System.out.println("Наши напитки: ");
        for (String line : vm.getDrinkTypes()) {
            System.out.println(line);
        }

        Scanner scan = new Scanner(System.in);
        printHelp();
        while (scan.hasNext()) {
            String command = scan.next();
            switch (command) {
                case "add": {
                    int money = scan.nextInt();
                    processAddMoney(money);
                    break;
                }
                case "get": {
                    int key = scan.nextInt();
                    processGetDrink(key);
                    break;
                }
                case "end": {
                    processEnd();
                    return;
                }
                default:
                    System.out.println("Команда не определена");
            }
            scan.nextLine();
            LOG.info("Действие пользователя: " + command);
        }
    }

    /**
     * обработка добавления денег в автомат
     * @param money - сумма
     */
    private static void processAddMoney(int money) {
        vm.addMoney(money);
        /*try {
            double jam = Math.random();
            if(jam < 0.3) {
                throw new JamException();
            } else {
                System.out.println("Текущий баланс: " + vm.addMoney(money));
            }
        } catch (JamException jamEx) {
            System.out.println(jamEx.getMessage() + " Заберите Ваши " + money + " у.е., они нам не нравятся!");
        }*/
    }

    /**
     * обработка получения напитка
     * @param key - код напитка в автомате
     */
    private static void processGetDrink(int key) {
        try {
            DrinkType drinkType = vm.giveMeADrink(key);
            if (drinkType != null) {
                System.out.println("Ммм! " + drinkType.getName() + "!");
                LOG.trace("Выдали напиток");
            } else {
                System.out.println("Напиток почему-то не получен...");
                LOG.warn("Напиток выдать не получилось!");
            }
        } catch (HumanFactorException humanFactorEx){
            System.out.println(humanFactorEx.getMessage());
            LOG.error("Внимание: " + humanFactorEx);
        } catch (OutOfStockException outOfStockEx) {
            System.out.println(outOfStockEx.getMessage());
            LOG.error("Предупреждение: " + outOfStockEx);
        }
    }

    /**
     * обработка получения сдачи
     */
    private static void processEnd() {
        System.out.println("Ваша сдача: " + vm.getChange());
        LOG.info("Выдали сдачу в размере " + vm.getChange());
    }

    /**
     * выводит подсказку по доступным командам
     */
    private static void printHelp() {
        System.out.println( "Введите 'add <количество>' для добавления купюр" );
        System.out.println( "Введите 'get <код напитка>' для получения напитка" );
        System.out.println( "Введите 'end' для получения сдачи" );
    }
}
