import java.util.Scanner;
import java.util.Vector;

class ProductService 
{
    private static Product[] products = 
    {
        new Product("Pen", 10),
        new Product("Notebook", 50),
        new Product("Eraser", 5),
        new Product("Marker", 15),
        new Product("Folder", 20),
        new Product("Pencil", 5),
        new Product("Highlighter", 20),
        new Product("Stapler", 55),
        new Product("Glue", 25),
        new Product("Scissors", 60)
    };

    public static Product[] getProducts() 
    {
        return products;
    }

    public static Product getProduct(int index) 
    {
        if (index >= 0 && index < products.length) 
        {
            return products[index];
        }
        return null;
    }

    public static void displayProducts() 
    {
        System.out.println("Available products:");
        for (int i = 0; i < products.length; i++) 
        {
            System.out.println((i + 1) + ". " + products[i]);
        }
    }
}

class Product 
{
    private String name;
    private int price;

    public Product(String name, int price) 
    {
        this.name = name;
        this.price = price;
    }

    public String getName() 
    {
        return name;
    }

    public int getPriceInINR() 
    {
        return price;
    }

    public String toString() 
    {
        return name + ": " + formatCurrency(getPriceInINR());
    }

    private String formatCurrency(int amount) 
    {
        return "Rupees " + String.format("%,d", amount);
    }
}

class Customer 
{
    private String name;

    public Customer(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
}

class OrderItem 
{
    private Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) 
    {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() 
    {
        return product;
    }

    public int getQuantity() 
    {
        return quantity;
    }

    public void updateQuantity(int delta) 
    {
        this.quantity += delta;
    }

    public int getTotalPriceInINR() 
    {
        return product.getPriceInINR() * quantity;
    }

    public String toString() 
    {
        return product.getName() + " (x" + quantity + "): " + formatCurrency(getTotalPriceInINR());
    }

    private String formatCurrency(int amount) 
    {
        return "Rupees " + String.format("%,d", amount);
    }
}

class Order 
{
    private Customer customer;
    private Vector<OrderItem> orderItems;
    private int totalAmount = 0;

    public Order(Customer customer) 
    {
        this.customer = customer;
        this.orderItems = new Vector<>();
    }

    public void addProduct(Product product, int quantity) 
    {
        boolean found = false;
        for (OrderItem item : orderItems) 
        {
            if (item.getProduct().equals(product)) 
            {
                item.updateQuantity(quantity);
                totalAmount += product.getPriceInINR() * quantity;

                if (item.getQuantity() <= 0) 
                {
                    orderItems.remove(item);
                    System.out.println("Removed " + product.getName() + " from the order.");
                }
                found = true;
                break;
            }
        }

        if (!found && quantity > 0) 
        {
            orderItems.add(new OrderItem(product, quantity));
            totalAmount += product.getPriceInINR() * quantity;
        }
    }

    public void printOrder() 
    {
        System.out.println("Order for " + customer.getName() + ":");
        for (OrderItem item : orderItems) 
        {
            System.out.println(item);
        }
        System.out.println("Total Amount: " + formatCurrency(totalAmount));
    }

    private String formatCurrency(int amount) 
    {
        return "Rupees " + String.format("%,d", amount);
    }
}

public class StationeryShopB 
{
    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine();
        Customer customer = new Customer(customerName);
        Order order = new Order(customer);

        ProductService.displayProducts();
        int productLimit = 50;
        int selectedProducts = 0;
        boolean finished = false;

        while (!finished && selectedProducts < productLimit) 
        {
            System.out.println("\nOptions:");
            System.out.println("1. Add a product");
            System.out.println("2. Remove a product");
            System.out.println("0. Finish order");
            System.out.print("Enter your choice: ");
            int option = scanner.nextInt();

            switch (option) 
            {
                case 1: 
                    ProductService.displayProducts();
                    System.out.print("Select a product by number: ");
                    int addChoice = scanner.nextInt();

                    if (addChoice > 0 && addChoice <= ProductService.getProducts().length) 
                    {
                        Product selectedProduct = ProductService.getProduct(addChoice - 1);
                        System.out.print("Enter quantity for " + selectedProduct.getName() + ": ");
                        int addQuantity = scanner.nextInt();

                        if (addQuantity > 0) 
                        {
                            order.addProduct(selectedProduct, addQuantity);
                            System.out.println("Added " + addQuantity + " x " + selectedProduct.getName() + " to your order.");
                            selectedProducts++;
                        } 
                        else 
                        {
                            System.out.println("Quantity must be greater than 0.");
                        }
                    } 
                    else 
                    {
                        System.out.println("Invalid product selection.");
                    }
                    break;

                case 2: 
                    System.out.println("\nYour current order:");
                    order.printOrder();
                    System.out.print("Select a product by number to remove: ");
                    int removeChoice = scanner.nextInt();

                    if (removeChoice > 0 && removeChoice <= ProductService.getProducts().length) 
                    {
                        Product selectedProduct = ProductService.getProduct(removeChoice - 1);
                        System.out.print("Enter quantity to remove for " + selectedProduct.getName() + ": ");
                        int removeQuantity = scanner.nextInt();

                        if (removeQuantity > 0) 
                        {
                            order.addProduct(selectedProduct, -removeQuantity);
                            System.out.println("Removed " + removeQuantity + " x " + selectedProduct.getName() + " from your order.");
                        } 
                        else 
                        {
                            System.out.println("Quantity must be greater than 0.");
                        }
                    } 
                    else 
                    {
                        System.out.println("Invalid product selection.");
                    }
                    break;

                case 0: 
                    finished = true;
                    break;

                default:
                    System.out.println("Invalid option. Please choose again.");
            }

            if (selectedProducts == productLimit) 
            {
                System.out.println("Product limit reached. You can select up to " + productLimit + " products.");
                finished = true;
            }
        }

        order.printOrder();
        scanner.close();
    }
}
