package functions;
import java.io.*;

public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable {

    public LinkedListTabulatedFunction() {
        head = new FunctionNode(null, null, null);
        head.setPrev(head);
        head.setNext(head);
        this.pointsCount = 0;
    }
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);
        FunctionNode current = head.getNext();
        while (current != head) {
            // Сериализуем только координаты x и y
            out.writeDouble(current.getPoint().getX());
            out.writeDouble(current.getPoint().getY());
            current = current.getNext();
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // Очищаем текущий список
        head = new FunctionNode(null, null, null);
        head.setPrev(head);
        head.setNext(head);
        pointsCount = 0;

        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            // Читаем координаты и создаем новые узлы
            double x = in.readDouble();
            double y = in.readDouble();
            addNodeToTail(new FunctionPoint(x, y));
        }
    }
    private class FunctionNode {
        private FunctionPoint point; // Данные узла - точка функции
        private FunctionNode prev; // Ссылка на предыдущий узел
        private FunctionNode next; // Ссылка на следующий узел

        public FunctionNode(FunctionPoint point, FunctionNode prev, FunctionNode next) { // Конструктор
            this.point = point;
            this.prev = prev;
            this.next = next;
        }

        public FunctionPoint getPoint() {
            return point;
        }

        public void setPoint(FunctionPoint point) {
            this.point = point;
        }

        public FunctionNode getPrev() {
            return prev;
        }

        public void setPrev(FunctionNode prev) {
            this.prev = prev;
        }

        public FunctionNode getNext() {
            return next;
        }

        public void setNext(FunctionNode next) {
            this.next = next;
        }
    }

    private FunctionNode head; // Голова списка
    private int pointsCount;

    // Конструктор 1
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница >= правой, левая: " + leftX + ", правая: " + rightX);
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть больше двух: " + pointsCount);
        }
        // Инициализация пустого списка с головой
        head = new FunctionNode(null, null, null);
        head.setPrev(head);
        head.setNext(head);
        this.pointsCount = 0;

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double currentX = leftX + i * step;
            addNodeToTail(new FunctionPoint(currentX, 0.0));
        }
    }

    // Конструктор 2
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница >= правой, левая: " + leftX + ", правая: " + rightX);
        }
        if (values == null || values.length < 2) {
            throw new IllegalArgumentException("Массив должен содержать не менее 2 элементов");
        }
        // Инициализация пустого списка с головой
        head = new FunctionNode(null, null, null);
        head.setPrev(head);
        head.setNext(head);
        this.pointsCount = 0;

        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            double currentX = leftX + i * step;
            addNodeToTail(new FunctionPoint(currentX, values[i]));
        }
    }
    //Конструктор 3
    public LinkedListTabulatedFunction(FunctionPoint[] points) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("Должно быть не менее 2 точек");
        }
        // Проверка упорядоченности точек по x
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].getX() >= points[i + 1].getX() - 1e-10) {
                throw new IllegalArgumentException("Точки не упорядочены по x");
            }
        }
        // Инициализация пустого списка с головой
        head = new FunctionNode(null, null, null);
        head.setPrev(head);
        head.setNext(head);
        this.pointsCount = 0;

        // Добавляем точки в список, создавая копии для инкапсуляции
        for (FunctionPoint point : points) {
            addNodeToTail(new FunctionPoint(point));
        }
    }

    public double getLeftDomainBorder() {
        if (pointsCount == 0)
            return Double.NaN;
        return head.getNext().getPoint().getX();
    }

    public double getRightDomainBorder() {
        if (pointsCount == 0)
            return Double.NaN;
        return head.getPrev().getPoint().getX();
    }

    public double getFunctionValue(double x) {
        if (pointsCount == 0) return Double.NaN;
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }

        FunctionNode current = head.getNext();
        while (current != head) {
            if (Math.abs(x - current.getPoint().getX()) < 1e-10) {
                return current.getPoint().getY();
            }
            current = current.getNext();
        }

        // Интерполяция
        current = head.getNext();
        for (int i = 0; i < pointsCount - 1; i++) {
            double x1 = current.getPoint().getX();
            double x2 = current.getNext().getPoint().getX();

            if (x >= x1 && x <= x2) {
                double y1 = current.getPoint().getY();
                double y2 = current.getNext().getPoint().getY();
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
            current = current.getNext();
        }
        return Double.NaN;
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public FunctionPoint getPoint(int index) {
        return new FunctionPoint(getNodeByIndex(index).getPoint());
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);

        if (index > 0 && point.getX() <= getNodeByIndex(index - 1).getPoint().getX() + 1e-10) {
            throw new InappropriateFunctionPointException("x должен быть больше предыдущего");
        }
        if (index < pointsCount - 1 && point.getX() >= getNodeByIndex(index + 1).getPoint().getX() - 1e-10) {
            throw new InappropriateFunctionPointException("x должен быть меньше следующего");
        }

        node.setPoint(new FunctionPoint(point));
    }

    public double getPointX(int index) {
        return getNodeByIndex(index).getPoint().getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        FunctionPoint currentPoint = getNodeByIndex(index).getPoint();
        FunctionPoint newPoint = new FunctionPoint(x, currentPoint.getY());
        setPoint(index, newPoint);
    }

    public double getPointY(int index) {
        return getNodeByIndex(index).getPoint().getY();
    }

    public void setPointY(int index, double y) {
        FunctionNode node = getNodeByIndex(index);
        FunctionPoint currentPoint = node.getPoint();
        node.setPoint(new FunctionPoint(currentPoint.getX(), y));
    }

    public void deletePoint(int index) {
        deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode current = head.getNext();
        int insertIndex = 0;

        while (current != head && current.getPoint().getX() < point.getX()) {
            current = current.getNext();
            insertIndex++;
        }

        if (current != head && Math.abs(current.getPoint().getX() - point.getX()) < 1e-10) {
            throw new InappropriateFunctionPointException("Точка с х " + point.getX() + " уже существует");
        }

        // Вставка новой точки
        addNodeByIndex(insertIndex, new FunctionPoint(point));
    }

    // Возвращает узел по указанному индексу
    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        // Линейный поиск узла
        FunctionNode current = head.getNext();
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current;
    }
    // Добавляет узел в конец списка
    private FunctionNode addNodeToTail(FunctionPoint point) {
        // Создаем новый узел между последним узлом и головой
        FunctionNode newNode = new FunctionNode(point, head.getPrev(), head);
        // Обновляем ссылки соседних узлов
        head.getPrev().setNext(newNode);
        head.setPrev(newNode);
        pointsCount++;
        return newNode;
    }
    // Добавляет узел по указанному индексу
    private FunctionNode addNodeByIndex(int index, FunctionPoint point) {
        if (index < 0 || index > pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        if (index == pointsCount) {
            return addNodeToTail(point);
        }
        // Вставка в середину списка
        FunctionNode nodeIndex = getNodeByIndex(index);
        FunctionNode newNode = new FunctionNode(point, nodeIndex.getPrev(), nodeIndex);

        nodeIndex.getPrev().setNext(newNode);
        nodeIndex.setPrev(newNode);
        pointsCount++;
        return newNode;
    }
    // Удаляет узел по указанному индексу
    private FunctionNode deleteNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        if (pointsCount <= 2) {
            throw new IllegalStateException("Не может быть удаленаБ должно быть >=2 точек");
        }
        // проходим сквозь удаляемый узел
        FunctionNode nodeToDelete = getNodeByIndex(index);
        nodeToDelete.getPrev().setNext(nodeToDelete.getNext());
        nodeToDelete.getNext().setPrev(nodeToDelete.getPrev());
        pointsCount--;
        return nodeToDelete;
    }
    public String toString() {
        if (pointsCount == 0) return "{}";

        String res = "";
        FunctionNode cur = head.getNext();

        while (cur != head) {
            res += cur.getPoint().toString();
            cur = cur.getNext();
            if (cur != head) {
                res += ", ";
            }
        }
        return "{" + res + "}";
    }
    public boolean equals(Object a) {
        if (this == a)
            return true;
        if (a == null || !(a instanceof TabulatedFunction))
            return false;

        TabulatedFunction b = (TabulatedFunction) a;
        if (this.getPointsCount() != b.getPointsCount()) return false;

        // для LinkedListTabulatedFunction
        if (a instanceof LinkedListTabulatedFunction) {
            LinkedListTabulatedFunction list = (LinkedListTabulatedFunction) a;
            FunctionNode nod1 = head.getNext();
            FunctionNode nod2 = list.head.getNext();

            while (nod1 != head) {
                if (!nod1.getPoint().equals(nod2.getPoint()))
                    return false;
                nod1 = nod1.getNext();
                nod2 = nod2.getNext();
            }
        } else {
            // Общий случай
            for (int i = 0; i < pointsCount; i++) {
                if (!this.getPoint(i).equals(b.getPoint(i)))
                    return false;
            }
        }
        return true;
    }
    public int hashCode() {
        int hash = pointsCount;
        FunctionNode cur = head.getNext();
        while (cur != head) {
            hash ^= cur.getPoint().hashCode();
            cur = cur.getNext();
        }
        return hash;
    }
    public Object clone() {
        LinkedListTabulatedFunction clone = new LinkedListTabulatedFunction();

        if (pointsCount == 0) return clone;

        FunctionNode lastNode = null;
        FunctionNode cur = head.getNext();

        while (cur != head) {
            FunctionPoint pointCopy = (FunctionPoint) cur.getPoint().clone();
            FunctionNode newNode = new FunctionNode(pointCopy, null, null);

            if (lastNode == null) {
                // Первый узел
                clone.head.setNext(newNode);
                newNode.setPrev(clone.head);
            } else {
                // следующие
                lastNode.setNext(newNode);
                newNode.setPrev(lastNode);
            }

            lastNode = newNode;
            cur = cur.getNext();
        }
        // Замыкаем
        if (lastNode != null) {
            lastNode.setNext(clone.head);
            clone.head.setPrev(lastNode);
        }

        clone.pointsCount = pointsCount;
        return clone;
    }
}